package com.tairanchina.csp.dew.core.cluster.spi.redis;

import com.tairanchina.csp.dew.core.cluster.ClusterDist;
import com.tairanchina.csp.dew.core.cluster.ClusterDistLock;
import com.tairanchina.csp.dew.core.cluster.ClusterDistMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("#{'${dew.cluster.cache}'=='redis' || '${dew.cluster.mq}'=='redis' || '${dew.cluster.dist}'=='redis'}")
public class RedisClusterDist implements ClusterDist {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ClusterDistLock lock(String key) {
        return new RedisClusterDistLock(key, redisTemplate);
    }

    @Override
    public <M> ClusterDistMap<M> map(String key, Class<M> clazz) {
        return new RedisClusterDistMap<>(key, clazz, redisTemplate);
    }


}
