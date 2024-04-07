package com.example.redisexample.service;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import java.util.List;

public interface OrderServiceSpringCache {

    OrderDto createOrder(CreateOrderRequest request);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getOrdersHistory();

    void deleteOrder(Long orderId);

    List<Order> getOrderByBrandName(String brandName);

}
