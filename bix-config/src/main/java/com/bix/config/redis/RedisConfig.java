package com.bix.config.redis;

//import com.bix.bixApi.utils.RedisUtil;
import com.bix.bixApi.utils.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class RedisConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate = null;

    @Value("${bixApi.redis.prefix}")
    private String prefix;


    @PostConstruct
    public void init() {
        initStringRedisTemplate();
    }

    @Bean
    public RedisSerializer<Object> fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<>(Object.class);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private void initStringRedisTemplate() {
        RedisSerializer<String> stringSerializer = stringRedisTemplate.getStringSerializer();
        stringRedisTemplate.setKeySerializer(stringSerializer);
        stringRedisTemplate.setValueSerializer(stringSerializer);
        stringRedisTemplate.setHashKeySerializer(stringSerializer);
        stringRedisTemplate.setKeySerializer(stringSerializer);
    }
//
//    @Bean
//    public RedisClient redisUtil(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
//        RedisClient redisUtil = new RedisClient();
//        redisUtil.setRedisTemplate(redisTemplate);
//        redisUtil.setStringRedisTemplate(stringRedisTemplate);
//        redisUtil.setNamespace(prefix);
//        return redisUtil;
//    }

}
