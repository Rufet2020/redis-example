package com.example.redisexample.controller;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import com.example.redisexample.service.OrderServiceSpringCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderControllerSpringCache {

    private final OrderServiceSpringCache serviceSpringCache;

    @PostMapping
    private ResponseEntity<OrderDto> createOrder(
            @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.ok(serviceSpringCache.createOrder(request));
    }

    @GetMapping
    private ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity.ok(serviceSpringCache.getOrdersHistory());
    }

    @GetMapping("/{id}")
    private ResponseEntity<OrderDto> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(serviceSpringCache.getOrderById(id));
    }

    @GetMapping("/brand/{brand}")
    private ResponseEntity<List<Order>> getOrdersByBrandName(
            @PathVariable(name = "brand") String brand
    ) {
        return ResponseEntity.ok(serviceSpringCache.getOrderByBrandName(brand));
    }

    @DeleteMapping("/{id}")
    private void deleteOrder(@PathVariable Long id) {
        serviceSpringCache.deleteOrder(id);
    }

}
