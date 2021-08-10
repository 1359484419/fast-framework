//package com.bix.bixApi.utils;
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.scheduling.annotation.Async;
//
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//@SuppressWarnings("unchecked")
//public class RedisUtil {
//    private RedisTemplate redisTemplate;
//    private StringRedisTemplate stringRedisTemplate;
//    private String namespace = "";
//
//
//    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }
//
//    public void setNamespace(String namespace) {
//        if (namespace != null)
//            this.namespace = namespace + ":";
//    }
//
//    public void set(String k, Object v, long time) {
//        String key = namespace + k;
//        if (v instanceof String && stringRedisTemplate != null) {
//            stringRedisTemplate.opsForValue().set(key, (String) v);
//        } else {
//            redisTemplate.opsForValue().set(key, v);
//        }
//        if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
//    }
//    public void set(String k, Object v) {
//        set(k, v, -1);
//    }
//
//    public boolean contains(String key) {
//        return redisTemplate.hasKey(namespace + key);
//    }
//
//    public String get(String k) {
//        if (stringRedisTemplate != null) {
//            return stringRedisTemplate.opsForValue().get(namespace + k);
//        } else {
//            return (String) redisTemplate.opsForValue().get(namespace + k);
//        }
//    }
//
//    public <T> T getObject(String k) {
//        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
//        return (T) valueOps.get(namespace + k);
//    }
//
//    public void remove(String key) {
//        redisTemplate.delete(namespace + key);
//    }
//
//    public long getExpire(String key) {
//        return redisTemplate.getExpire(key);
//    }
//
//    public Set<String> keys(String pattern) {
//        return redisTemplate.keys(namespace + pattern);
//    }
//
//    public Long increment(String key, long delta) {
//        return redisTemplate.opsForValue().increment(namespace + key, delta);
//    }
//
//    public Double increment(String key, double delta) {
//        return redisTemplate.opsForValue().increment(namespace + key, delta);
//    }
//}
