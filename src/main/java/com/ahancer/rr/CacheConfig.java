package com.ahancer.rr;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.ahancer.rr.constants.ApplicationConstant;

@Configuration
@EnableCaching
public class CacheConfig {
	@Value("${redis.cache.host}")
	private String redisHost;
	@Value("${redis.cache.port}")
	private Integer redisPort;
	@Value("${redis.cache.expiration.second}")
	private Integer redisExpiration;
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        // Defaults
        redisConnectionFactory.setHostName(redisHost);
        redisConnectionFactory.setPort(redisPort);
        return redisConnectionFactory;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }
    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setCacheNames(Arrays.asList(ApplicationConstant.UserRequestCache));
        // Number of seconds before expiration. Defaults to unlimited (0)
        cacheManager.setDefaultExpiration(redisExpiration);
        return cacheManager;
    }
}
