package com.example.redisexample.util.helper;

import static com.example.redisexample.util.constants.EMPTY_STRING;

import com.example.redisexample.model.order.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class SearchHelper {

    public static boolean orderContainsText(OrderDto orderDto, String searchText) {
        if (orderDto == null || searchText == null) {
            return false;
        }
        String id = orderDto.getId() != null ? orderDto.getId().toString() : EMPTY_STRING;
        String brandName = orderDto.getBrandName() != null ? orderDto.getBrandName() : EMPTY_STRING;
        String description = orderDto.getDescription() != null ? orderDto.getDescription() : EMPTY_STRING;
        String name = orderDto.getName() != null ? orderDto.getName() : EMPTY_STRING;
        return id.contains(searchText) || brandName.contains(searchText) || description.contains(searchText) ||
                name.contains(searchText);
    }

}
