package com.example.redisexample.mapper;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;


@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface OrderMapper {

    @Mapping(target = "firstName", source = "name")
    OrderDto toOrderDto(Order order);

    Order toOrderEntity(CreateOrderRequest request);

}
