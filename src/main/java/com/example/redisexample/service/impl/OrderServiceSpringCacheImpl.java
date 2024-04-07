package com.example.redisexample.service.impl;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.dao.repository.OrderRepository;
import com.example.redisexample.exception.OrderNotFoundException;
import com.example.redisexample.mapper.OrderMapper;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import com.example.redisexample.service.OrderServiceSpringCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceSpringCacheImpl implements OrderServiceSpringCache {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    // Create methodu üçün CachePut və ya CacheEvict işlədilə bilər
    // CachePut ilə ümumi cache update olunur bu method ilə elaqeli
    // CacheEvict ile bu cache ile əlaqəli cachelər silinəcək
    @Override
    @CachePut(value = "orders", key = "#result.id", unless = "#result.brandName == null")
    public OrderDto createOrder(CreateOrderRequest request) {
        return orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrderEntity(request)));
    }

    // Caching for simple method
    // unless = "#result.price > 500" cache ləmə üçün müəyyən şərt qoya bilərik
    @Override
    @Cacheable(value = "orders", key = "#orderId")
    public OrderDto getOrderById(Long orderId) {
        return orderRepository.findOrderById(orderId).map(orderMapper::toOrderDto)
                .orElseThrow(OrderNotFoundException::new);
    }

    // Caching for expensive || frequently accessed data
    @Override
    @Cacheable(value = "orders", keyGenerator = "customCacheKeyGenerator")
    public List<OrderDto> getOrdersHistory() {
        return orderRepository.findAll().stream().map(orderMapper::toOrderDto).toList();
    }

    // Delete from cache
    // "allEntries = true" burda bu orderin cahce'ləndiyi bütün dataları silir.
    // Məs: Butun orderler listi cahce'lənmişdisə, bu Order silindikdə o cache də təmizlənəcək
    @Override
    @CacheEvict(value = "orders", allEntries = true)
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    @Cacheable(value = "orders", key = "{#brandName}")
    public List<Order> getOrderByBrandName(String brandName) {
        return orderRepository.findAllByBrandName(brandName);
    }

}
