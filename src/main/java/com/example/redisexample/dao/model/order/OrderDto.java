package com.example.redisexample.dao.model.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDto {
    private Long id;
    private String name;
    private Long quantity;
    private String brandName;
    private BigDecimal price;
    private boolean isDeleted;
    private String description;
}
