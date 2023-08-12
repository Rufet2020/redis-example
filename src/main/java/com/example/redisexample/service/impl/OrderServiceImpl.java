package com.example.redisexample.service.impl;

import com.example.redisexample.dao.entity.Order;
import com.example.redisexample.dao.repository.OrderRepository;
import com.example.redisexample.exception.OrderNotFoundException;
import com.example.redisexample.mapper.OrderMapper;
import com.example.redisexample.model.order.OrderDto;
import com.example.redisexample.model.order.request.CreateOrderRequest;
import com.example.redisexample.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    // Bu layerde caching muxtelif tip obyektler uzerinde olacaqsa bu versiya daha optimaldir.
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @CachePut(value = "orders", key = "#result.id")
    public OrderDto createOrder(CreateOrderRequest request) {
        OrderDto orderDto = orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrderEntity(request)));
        log.info("created orderDto with id of: {}", orderDto.getId());
        return orderDto;
    }

    // Caching for simple method V1
    @Override
    @Cacheable(value = "orders", key = "#orderId")
    public OrderDto getOrderById(Long orderId) {
        return orderRepository.findOrderById(orderId)
                .map(orderMapper::toOrderDto)
                .orElseThrow(OrderNotFoundException::new);
    }


    // Caching for simple method V2
//    @Override
//    public OrderDto getOrderById(Long orderId) {
//        String cacheKey = "order" + orderId;
//        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
//
//        OrderDto order = (OrderDto) valueOps.get(cacheKey);
//        if (order == null) {
//            Optional<Order> orderOptional = orderRepository.findOrderById(orderId);
//            order = orderOptional.map(orderMapper::toOrderDto)
//                    .orElseThrow(OrderNotFoundException::new);
//            valueOps.set(cacheKey, order);
//        }
//
//        return order;
//    }

    //Caching for expensive method V1
    @Override
    @Cacheable(value = "orders", keyGenerator = "customCacheKeyGenerator")
    public List<Order> getAllOrdersV1() {
        List<Order> orders = orderRepository.findAll();
        System.out.println(orders.size());
        return orders.stream()
                .filter(order -> order.getBrandName().length() < 10)
                .peek(order -> {
                    var quantity = order.getQuantity() == null ? 1L : order.getQuantity() + 2;
                    var price = order.getPrice() == null ? BigDecimal.ZERO
                            : order.getPrice().setScale(1, RoundingMode.HALF_DOWN);
                    order.setBrandName(String.format("%s %s", order.getBrandName(), order.getName()));
                    order.setQuantity(quantity);
                    order.setPrice(price);
                }).toList();
    }


    // Caching for frequently accessed data v2
    @Override
    public List<OrderDto> getAllOrdersV2() {
        String cacheKey = "orders";
        ListOperations<String, Object> listOps = redisTemplate.opsForList();

        List<Object> cachedOrders = listOps.range(cacheKey, 0, -1);

        if (cachedOrders != null && !cachedOrders.isEmpty()) {
            // Orders found in cache
            return cachedOrders.stream()
                    .map(cachedOrder -> (OrderDto) cachedOrder)
                    .collect(Collectors.toList());
        }

        // Orders not found in cache, fetch from the repository
        List<OrderDto> orders = orderRepository.findAll().stream()
                .filter(order -> order.getBrandName().length() < 10)
                .peek(order -> {
                    var quantity = order.getQuantity() == null ? 1L : order.getQuantity() + 2;
                    var price = order.getPrice() == null ? BigDecimal.ZERO
                            : order.getPrice().setScale(1, RoundingMode.HALF_DOWN);
                    order.setBrandName(String.format("%s %s", order.getBrandName(), order.getName()));
                    order.setQuantity(quantity);
                    order.setPrice(price);
                })
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());

        // Cache for next time (only if there are orders to cache)
        if (!orders.isEmpty()) {
            listOps.rightPushAll(cacheKey, orders.toArray());
            // Set a TTL on the cache to expire in 1 hour
            redisTemplate.expire(cacheKey, Duration.ofHours(1));
        }
        System.out.println(orders.size());
        return orders;
    }

    //All entries
    @Override
    @CacheEvict(value = "orders")
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }


    @Override
    @Cacheable(value = "orders", key = "{#brandName}")
    public List<Order> getOrderByBrandName(String brandName) {
        List<Order> orders = orderRepository.findAllByBrandName(brandName);

        return orders.stream()
                .filter(order -> order.getCreatedAt().getMonth().name().equals(LocalDate.now().getMonth().name()))
                .peek(order -> {
                    order.setBrandName(order.getBrandName().substring(0, 3));
                    String name = order.getName() == null
                            ? "Luxury Brand"
                            : String.format("Luxury %s %s", order.getName(), order.getBrandName());
                    order.setName(name);
                }).collect(Collectors.toList());
    }

    @Override
    public List<Order> getOrderByBrandNameAndName(String brandName, String name) {
        return orderRepository.findAllByBrandNameAndName(brandName, name);
    }
}
