package com.example.redisexample.service.impl;

import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.service.OrderCacheService;
import com.example.redisexample.util.CacheUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCacheServiceImpl implements OrderCacheService {

    private final CacheUtils cacheUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public OrderDto getOrderById(Long orderId) {
        String cacheKey = "order::" + orderId;
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return (OrderDto) valueOps.get(cacheKey);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        String cacheKey = "orders";
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> cachedOrders = listOps.range(cacheKey, 0, -1);
        if (cachedOrders != null && !cachedOrders.isEmpty()) {
            return cachedOrders.stream().map(cachedOrder -> (OrderDto) cachedOrder).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void cacheOrder(OrderDto order) {
        String cacheKey = "order::" + order.getId();
        cacheUtils.cacheObject(cacheKey, order);
    }

    @Override
    public void cacheOrders(List<OrderDto> orders) {
        String cacheKey = "orders";
        cacheUtils.cacheList(cacheKey, orders);
    }

}
