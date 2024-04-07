package com.example.redisexample.service.impl;

import static com.example.redisexample.util.Constants.ORDERS_KEY;
import static com.example.redisexample.util.Constants.ORDER_KEY;

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
        String cacheKey = ORDER_KEY + orderId;
        return (OrderDto) redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Object> cachedOrders = redisTemplate.opsForList().range(ORDERS_KEY, 0, -1);
        if (cachedOrders != null && !cachedOrders.isEmpty()) {
            return cachedOrders.stream().map(cachedOrder -> (OrderDto) cachedOrder).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void cacheOrders(List<OrderDto> orders) {
        if (orders.size() == 1) {
            String cacheKey = ORDER_KEY + orders.get(0).getId();
            cacheUtils.cacheObject(cacheKey, orders.get(0));
        } else {
            cacheUtils.cacheList(ORDERS_KEY, orders);
        }
    }

}
