package com.example.redisexample.controller;

import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import com.example.redisexample.service.OrderServiceManualCache;
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
@RequestMapping("/v2/order")
public class OrderControllerManualCache {

    private final OrderServiceManualCache serviceManualCache;

    @PostMapping
    private ResponseEntity<OrderDto> createOrder(
            @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.ok(serviceManualCache.createOrder(request));
    }

    @GetMapping
    private ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity.ok(serviceManualCache.getAllOrders());
    }

    @GetMapping("/{id}")
    private ResponseEntity<OrderDto> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(serviceManualCache.getOrderById(id));
    }

    @GetMapping("/search/{query}")
    private ResponseEntity<List<OrderDto>> searchOrderWithoutCache(@PathVariable String query) {
        return ResponseEntity.ok(serviceManualCache.searchOrderWithoutCache(query));
    }

    @GetMapping("/search-cache/{query}")
    private ResponseEntity<List<OrderDto>> searchOrderWithCache(@PathVariable String query) {
        return ResponseEntity.ok(serviceManualCache.searchOrderWithCache(query));
    }

    @DeleteMapping("/{id}")
    private void deleteOrder(@PathVariable Long id) {
        serviceManualCache.deleteOrder(id);
    }

}
