package com.example.redisexample.model.order.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {
    private String name;
    private Long quantity;
    private String brandName;
    private BigDecimal price;
    private String description;
}
