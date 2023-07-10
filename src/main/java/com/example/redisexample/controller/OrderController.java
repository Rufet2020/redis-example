package com.example.redisexample.controller;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.dao.model.order.OrderDto;
import com.example.redisexample.dao.model.order.request.CreateOrderRequest;
import com.example.redisexample.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    @PostMapping
    private ResponseEntity<OrderDto> createOrder(
            @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.ok(service.createOrder(request));
    }

    @GetMapping()
    private ResponseEntity<List<Order>> getById() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @GetMapping("/{id}")
    private ResponseEntity<Order> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/brand/{brand}")
    private ResponseEntity<List<Order>> getOrdersByBrandName(
            @PathVariable(name = "brand") String brand
    ) {
        return ResponseEntity.ok(service.getOrderByBrandName(brand));
    }

    @GetMapping("/brand/{brand}/{name}")
    private ResponseEntity<List<Order>> getOrdersByBrandName(
            @PathVariable(name = "brand") String brand,
            @PathVariable(name = "name") String name
    ) {
        return ResponseEntity.ok(service.getOrderByBrandNameAndName(brand, name));
    }

}
