package com.example.redisexample.service.impl;

import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.service.OrderCacheService;
import com.example.redisexample.util.CacheUtils;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCacheServiceImpl implements OrderCacheService {

    private final CacheUtils cacheUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public OrderDto getOrderById(Long orderId) {
        String cacheKey = "order::" + orderId;
        return (OrderDto) redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        String cacheKey = "orders";
        List<Object> cachedOrders = redisTemplate.opsForList().range(cacheKey, 0, -1);
        if (cachedOrders != null && !cachedOrders.isEmpty()) {
            return cachedOrders.stream().map(cachedOrder -> (OrderDto) cachedOrder).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void cacheOrders(List<OrderDto> orders) {
        if (orders.size() == 1) {
            String cacheKey = "order::" + orders.get(0).getId();
            cacheUtils.cacheObject(cacheKey, orders.get(0));
        } else {
            String cacheKey = "orders";
            cacheUtils.cacheList(cacheKey, orders);
        }
    }

}
