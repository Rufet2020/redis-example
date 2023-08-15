package com.example.redisexample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@ConfigurationProperties("redis")
@EnableConfigurationProperties(CacheConfig.class)
public class CacheConfig {

    @Value("${spring.cache.redis.port}")
    private Integer port;

    @Value("${spring.cache.redis.host}")
    private String host;

    @Primary
    @Bean(name = {"JedisConnectionFactory"})
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        return new JedisConnectionFactory(redisConfig);
    }


    @Primary
    @Bean(name = {"RedisTemplate"})
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        var genericRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(genericRedisSerializer);
        template.setValueSerializer(genericRedisSerializer);
        //Todo: Serializator numnesi kimi goster
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new LocalDateTimeSerializer());
        template.setHashValueSerializer(genericRedisSerializer);
        template.setHashKeySerializer(genericRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
