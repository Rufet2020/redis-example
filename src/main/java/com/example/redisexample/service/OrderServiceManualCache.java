package com.example.redisexample.service;

import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import java.util.List;

public interface OrderServiceManualCache {

    OrderDto createOrder(CreateOrderRequest request);


    OrderDto getOrderById(Long orderId);


    List<OrderDto> getAllOrders();


    void deleteOrder(Long orderId);

    List<OrderDto> searchOrderWithCache(String searchText);

    List<OrderDto> searchOrderWithoutCache(String searchText);

}
