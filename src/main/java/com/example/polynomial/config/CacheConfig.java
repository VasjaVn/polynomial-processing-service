package com.example.polynomial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class CacheConfig {

    @Bean
    public RedisTemplate<String, Double> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Double> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
