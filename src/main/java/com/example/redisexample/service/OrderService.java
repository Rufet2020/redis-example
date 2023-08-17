package com.example.redisexample.service;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    OrderDto createOrderV1(CreateOrderRequest request);

    OrderDto createOrderV2(CreateOrderRequest request);

    OrderDto getOrderByIdV1(Long orderId);

    OrderDto getOrderByIdV2(Long orderId);

    List<OrderDto> getAllOrdersV1();

    List<OrderDto> getAllOrdersV2();

    void deleteOrderV1(Long orderId);

    void deleteOrderV2(Long orderId);

    List<OrderDto> searchOrderV1(String searchText);

    List<OrderDto> searchOrderV2(String searchText);


    List<Order> getOrderByBrandName(String brandName);

    List<Order> getOrderByBrandNameAndName(String brandName, String name);

}
