package com.example.redisexample.service;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.dao.model.order.OrderDto;
import com.example.redisexample.dao.model.order.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest request);

    Order getOrderById(Long orderId);

    List<Order> getAllOrders();

    List<Order> getOrderByBrandName(String brandName);

    List<Order> getOrderByBrandNameAndName(String brandName, String name);

}
