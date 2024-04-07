package com.example.redisexample.service.impl;

import static com.example.redisexample.util.helper.SearchHelper.orderContainsText;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.dao.repository.OrderRepository;
import com.example.redisexample.exception.OrderNotFoundException;
import com.example.redisexample.mapper.OrderMapper;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import com.example.redisexample.service.OrderCacheService;
import com.example.redisexample.service.OrderServiceManualCache;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceManualCacheImpl implements OrderServiceManualCache {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderCacheService orderCacheService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        OrderDto orderDto = orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrderEntity(request)));
        log.info("created orderDto with id of: {}", orderDto.getId());
        orderCacheService.cacheOrder(orderDto);
        orderCacheService.cacheOrders(List.of(orderDto));
        return orderDto;
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        OrderDto orderDto = orderCacheService.getOrderById(orderId);
        if (orderDto == null) {
            Optional<Order> orderOptional = orderRepository.findOrderById(orderId);
            orderDto = orderOptional.map(orderMapper::toOrderDto).orElseThrow(OrderNotFoundException::new);
            orderCacheService.cacheOrder(orderDto);
        }
        return orderDto;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        // Check from cache
        List<OrderDto> orders = orderCacheService.getAllOrders();
        if (orders.isEmpty()) {
            // Cache is empty then get from DB
            orders = orderRepository.findAll().stream().map(orderMapper::toOrderDto).toList();
            // Cache result
            orderCacheService.cacheOrders(orders);
        }
        return orders;
    }

    // Delete from cache
    // "allEntries = true" burda bu orderin cahce'ləndiyi bütün dataları silir.
    // Məs: Butun orderler listi cahce'lənmişdisə, bu Order silindikdə o cache də təmizlənəcək
    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        // Delete from getOrderById cache
        redisTemplate.delete("order::" + orderId);
        //Delete from getAllOrders cache
        redisTemplate.delete("orders");
    }

    // Caching search result
    @Override
    public List<OrderDto> searchOrderWithCache(String searchText) {
        String cacheKey = "search:" + searchText;
        ListOperations<String, Object> listOps = redisTemplate.opsForList();

        // Try to fetch results from cache
        List<Object> cachedOrders = listOps.range(cacheKey, 0, -1);
        if (cachedOrders != null && !cachedOrders.isEmpty()) {
            return cachedOrders.stream()
                    .filter(cachedOrder -> cachedOrder instanceof OrderDto)
                    .map(cachedOrder -> (OrderDto) cachedOrder)
                    .toList();
        }
        List<OrderDto> orderDtoList = orderRepository.findAll().stream().map(orderMapper::toOrderDto).toList();
        List<OrderDto> searchResults = orderDtoList.parallelStream()
                .filter(order -> orderContainsText(order, searchText)).toList();
        // Cache the search results
        listOps.leftPushAll(cacheKey, searchResults.toArray());
        return searchResults;
    }

    @Override
    public List<OrderDto> searchOrderWithoutCache(String searchText) {
        return orderRepository.findAll().stream().map(orderMapper::toOrderDto).toList().parallelStream()
                .filter(order -> orderContainsText(order, searchText))
                .collect(Collectors.toList());
    }

}
