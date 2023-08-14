package com.example.redisexample.model.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDto implements Serializable {
    private Long id;
    private String name;
    private Long quantity;
    private String brandName;
    private BigDecimal price;
    private boolean isDeleted;
    private String description;
}
