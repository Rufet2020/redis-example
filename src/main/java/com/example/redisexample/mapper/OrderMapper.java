package com.example.redisexample.mapper;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;


@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order toOrderEntity(CreateOrderRequest request);

}
