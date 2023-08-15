package com.example.redisexample.service;

import com.example.redisexample.model.order.OrderDto;

import java.util.List;

public interface OrderCacheService {

    OrderDto getOrderById(Long orderId);
    List<OrderDto> getAllOrders();

    void cacheOrder(OrderDto orderDto);

    void cacheOrders(List<OrderDto> orders);

}
