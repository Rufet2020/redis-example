package com.example.redisexample.util;

import com.example.redisexample.model.order.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CacheUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> void cacheObject(String cacheKey, T object) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(cacheKey, object);
        redisTemplate.expire(cacheKey, Duration.ofHours(1));
    }

    public <T> void cacheList(String cacheKey, List<T> list) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.rightPushAll(cacheKey, list.toArray());
        redisTemplate.expire(cacheKey, Duration.ofHours(1));
    }

}
