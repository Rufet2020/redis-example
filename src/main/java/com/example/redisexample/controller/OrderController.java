package com.example.redisexample.controller;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
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

    @GetMapping("/all/v1")
    private ResponseEntity<List<Order>> getAllV1() {
        return ResponseEntity.ok(service.getAllOrdersV1());
    }

    @GetMapping("/all/v2")
    private ResponseEntity<List<OrderDto>> getAllV2() {
        return ResponseEntity.ok(service.getAllOrdersV2());
    }

    @GetMapping("/v1/{id}")
    private ResponseEntity<OrderDto> getByIdV1(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getOrderByIdV1(id));
    }

    @GetMapping("/v2/{id}")
    private ResponseEntity<OrderDto> getByIdV2(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getOrderByIdV2(id));
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

    @GetMapping("/v1/search/{query}")
    private ResponseEntity<List<OrderDto>> searchOrderV1(@PathVariable String query) {
        return ResponseEntity.ok(service.searchOrderV1(query));
    }

    @GetMapping("/v2/search/{query}")
    private ResponseEntity<List<OrderDto>> searchOrderV2(@PathVariable String query) {
        return ResponseEntity.ok(service.searchOrderV2(query));
    }

    @DeleteMapping("/v1/delete/{id}")
    private void deleteOrderV1(@PathVariable Long id) {
        service.deleteOrderV1(id);
    }

    @DeleteMapping("/v2/delete/{id}")
    private void deleteOrderV2(@PathVariable Long id) {
        service.deleteOrderV2(id);
    }

}
