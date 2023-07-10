package com.example.redisexample.service.impl;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.dao.model.order.OrderDto;
import com.example.redisexample.dao.model.order.request.CreateOrderRequest;
import com.example.redisexample.dao.repository.OrderRepository;
import com.example.redisexample.exception.OrderNotFoundException;
import com.example.redisexample.mapper.OrderMapper;
import com.example.redisexample.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        OrderDto orderDto = orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrderEntity(request)));
        log.info("created orderDto with id of: {}", orderDto.getId());
        return orderDto;
    }

    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findOrderById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new OrderNotFoundException();
        }
    }

    @Override
    @Cacheable("orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Cacheable("orderByBrandName")
    public List<Order> getOrderByBrandName(String brandName) {
        return orderRepository.findAllByBrandName(brandName);
    }

    @Override
    public List<Order> getOrderByBrandNameAndName(String brandName, String name) {
        return orderRepository.findAllByBrandNameAndName(brandName, name);
    }
}
