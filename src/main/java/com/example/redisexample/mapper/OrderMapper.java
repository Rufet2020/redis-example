package com.example.redisexample.mapper;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.dao.model.order.OrderDto;
import com.example.redisexample.dao.model.order.request.CreateOrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order toOrderEntity(CreateOrderRequest request);

}
