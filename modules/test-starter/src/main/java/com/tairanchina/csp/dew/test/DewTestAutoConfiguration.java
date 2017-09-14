package com.tairanchina.csp.dew.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class DewTestAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DewTestAutoConfiguration.class);

    private RedisServer redisServer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() throws IOException {
        logger.info("Enabled Dew Test");
        redisServer = new RedisServer();
        if (!redisServer.isActive()) {
            try {
                redisServer.start();
                redisTemplate.getConnectionFactory().getConnection();
            } catch (Throwable e) {
                logger.error("Start embedded redis error.", e);
            }
        }
    }


    @PreDestroy
    public void destroy() {
        if (redisServer.isActive()) {
            redisServer.stop();
        }
    }
}