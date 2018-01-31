package com.tairanchina.csp.dew.core.cluster.spi.redis;

import com.tairanchina.csp.dew.core.cluster.ClusterCache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class RedisClusterCache implements ClusterCache {

    private RedisTemplate<String, String> redisTemplate;

    public RedisClusterCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    @Deprecated
    public void set(String key, String value, long expireSec) {
        redisTemplate.opsForValue().set(key, value, expireSec, TimeUnit.SECONDS);
    }

    @Override
    public void setex(String key, String value, long expireSec) {
        redisTemplate.opsForValue().set(key, value, expireSec, TimeUnit.SECONDS);
    }

    @Override
    public boolean setnx(String key, String value, long expireSec) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            if (expireSec != 0) {
                expire(key, expireSec);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }


    @Override
    public void lmset(String key, List<String> values) {
        lmset(key, values, 0);
    }

    @Override
    public void lmset(String key, List<String> values, long expireSec) {
        redisTemplate.opsForList().leftPushAll(key, values);
        if (expireSec != 0) {
            expire(key, expireSec);
        }
    }

    @Override
    public void lpush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public String lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public List<String> lget(String key) {
        return redisTemplate.opsForList().range(key, 0, llen(key));
    }

    @Override
    public void smset(String key, List<String> values) {
        smset(key, values, 0);
    }

    @Override
    public void smset(String key, List<String> values, long expireSec) {
        redisTemplate.opsForSet().add(key, values.toArray(new String[]{}));
        if (expireSec != 0) {
            expire(key, expireSec);
        }
    }

    @Override
    public void sset(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public String spop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    @Override
    public long slen(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public long sdel(String key, String... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public Set<String> sget(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public void hmset(String key, Map<String, String> items) {
        hmset(key, items, 0);
    }

    @Override
    public void hmset(String key, Map<String, String> items, long expireSec) {
        redisTemplate.opsForHash().putAll(key, items);
        if (expireSec != 0) {
            expire(key, expireSec);
        }
    }

    @Override
    public void hset(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public String hget(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key)
                .entrySet().stream().collect(
                        Collectors.toMap(i -> (String) (i.getKey()), i -> (String) (i.getValue())));
    }

    @Override
    public Set<String> hkeys(String key) {
        return redisTemplate.opsForHash().keys(key)
                .stream().map(i -> (String) i).collect(Collectors.toSet());
    }

    @Override
    public Set<String> hvalues(String key) {
        return redisTemplate.opsForHash().values(key)
                .stream().map(i -> (String) i).collect(Collectors.toSet());
    }

    @Override
    public long hlen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public boolean hexists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }


    @Override
    public void hdel(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public long incrBy(String key, long incrValue) {
        return redisTemplate.opsForValue().increment(key, incrValue);
    }

    @Override
    public long decrBy(String key, long decrValue) {
        return redisTemplate.opsForValue().increment(key, -decrValue);
    }

    @Override
    public void expire(String key, long expireSec) {
        redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
    }

    @Override
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public void flushdb() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Override
    public boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key,offset,value);
    }

    @Override
    public boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key,offset);
    }
}
