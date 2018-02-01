package com.tairanchina.csp.dew.core.cluster.spi.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * desription:
 * Created by ding on 2018/1/25.
 */
@Configuration
public class RabbitAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RabbitAutoConfiguration.class);

    @PostConstruct
    private void init(){
        logger.info("Load Auto Configuration : {}", this.getClass().getName());
    }

    @Bean
    @ConditionalOnExpression("'${dew.cluster.mq}'=='rabbit'")
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public RabbitAdapter rabbitAdapter(RabbitTemplate rabbitTemplate) {
        return new RabbitAdapter(rabbitTemplate);
    }

    @Bean
    @ConditionalOnBean(RabbitAdapter.class)
    public RabbitClusterMQ rabbitClusterMQ(RabbitAdapter rabbitAdapter) {
        return new RabbitClusterMQ(rabbitAdapter);
    }
}
