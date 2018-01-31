package com.tairanchina.csp.dew.core.cluster.spi.ignite;

import com.tairanchina.csp.dew.core.cluster.ClusterMQ;

import java.util.function.Consumer;

public class IgniteClusterMQ implements ClusterMQ {

    private IgniteAdapter igniteAdapter;

    public IgniteClusterMQ(IgniteAdapter igniteAdapter) {
        this.igniteAdapter = igniteAdapter;
    }

    @Override
    public boolean publish(String topic, String message) {
        logger.trace("[MQ] publish {}:{}", topic, message);
        igniteAdapter.getIgnite().message(igniteAdapter.getIgnite().cluster().forRemotes()).send(topic, message);
        return true;
    }

    @Override
    public void subscribe(String topic, Consumer<String> consumer) {
        igniteAdapter.getIgnite().message(igniteAdapter.getIgnite().cluster().forRemotes())
                .remoteListen(topic, (nodeId, message) -> {
                    try {
                        String msg = (String) message;
                        logger.trace("[MQ] subscribe {}:{}", topic, msg);
                        consumer.accept(msg);
                        return true;
                    } catch (Exception e) {
                        logger.error("Ignite Subscribe error.", e);
                        return false;
                    }
                });
    }

    @Override
    public boolean request(String address, String message) {
        logger.trace("[MQ] request {}:{}", address, message);
        return igniteAdapter.getIgnite().queue(address, 0, null).add(message);
    }

    @Override
    public void response(String address, Consumer<String> consumer) {
        new Thread(() -> {
            try {
                while (true) {
                    String message = (String) igniteAdapter.getIgnite().queue(address, 0, null).take();
                    logger.trace("[MQ] response {}:{}", address, message);
                    consumer.accept(message);
                }
            } catch (Exception e) {
                logger.error("Ignite Response error.", e);
            }
        }).start();
    }

}
