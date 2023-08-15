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
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.redisexample.util.constants.EMPTY_STRING;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    // Bu layerde caching muxtelif tip obyektler uzerinde olacaqsa bu versiya daha optimaldir.
    private final RedisTemplate<String, Object> redisTemplate;


    // Create methodu üçün CachePut və ya CacheEvict işlədilə bilər
    // CachePut ilə ümumi cache update olunur bu entity ilə elaqeli
    // CacheEvict ile bu cache ile əlaqəli cachelər silinəcək
    @Override
    @CachePut(value = "orders", key = "#result.id", unless = "#result.brandName == null")
    public OrderDto createOrder(CreateOrderRequest request) {
        OrderDto orderDto = orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrderEntity(request)));
        log.info("created orderDto with id of: {}", orderDto.getId());
        return orderDto;
    }

    // Caching for simple method V1
    // unless = "#result.price > 500" cache ləmə üçün müəyyən şərt qoya bilərik
    @Override
    @Cacheable(value = "orders", key = "#orderId")
    public OrderDto getOrderByIdV1(Long orderId) {
        return orderRepository.findOrderById(orderId)
                .map(orderMapper::toOrderDto)
                .orElseThrow(OrderNotFoundException::new);
    }


    // Caching for simple method V2
    @Override
    public OrderDto getOrderByIdV2(Long orderId) {
        String cacheKey = "order::" + orderId;
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();

        OrderDto order = (OrderDto) valueOps.get(cacheKey);
        if (order == null) {
            Optional<Order> orderOptional = orderRepository.findOrderById(orderId);
            order = orderOptional.map(orderMapper::toOrderDto)
                    .orElseThrow(OrderNotFoundException::new);
            valueOps.set(cacheKey, order);
        }

        return order;
    }

    // Caching for expensive || frequently accessed data V1
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

    // Caching for expensive || frequently accessed data v2
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

    // Delete from cache
    // "allEntries = true" burda bu orderin cahce'ləndiyi bütün dataları silir.
    // Məs: Butun orderler listi cahce'lənmişdisə, bu Order silindikdə o cache də təmizlənəcək
    @Override
    @CacheEvict(value = "orders", allEntries = true)
    public void deleteOrderV1(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void deleteOrderV2(Long orderId) {
        orderRepository.deleteById(orderId);

        // Delete from getOrderById cache
        redisTemplate.delete("order::" + orderId);

        //Delete from getAllOrders cache
        redisTemplate.delete("orders");
    }


    // Caching search result
    @Override
    public List<OrderDto> searchOrderV1(String searchText) {
        String cacheKey = "search:" + searchText;
        ListOperations<String, Object> listOps = redisTemplate.opsForList();

        // Try to fetch results from cache
        List<Object> cachedOrders = listOps.range(cacheKey, 0, -1);
        if (cachedOrders != null && !cachedOrders.isEmpty()) {
            return getCachedSearchResult(cachedOrders);
        }

        List<OrderDto> orderDtoList = orderRepository.findAll().stream().map(orderMapper::toOrderDto).toList();

        List<OrderDto> searchResults = orderDtoList.parallelStream()
                .filter(order -> orderContainsText(order, searchText))
                .collect(Collectors.toList());

        // Cache the search results
        listOps.leftPushAll(cacheKey, searchResults.toArray());
        return searchResults;
    }

    // Search order without caching
    @Override
    public List<OrderDto> searchOrderV2(String searchText) {
        List<OrderDto> orderDtoList = orderRepository.findAll().stream().map(orderMapper::toOrderDto).toList();
        return orderDtoList.parallelStream()
                .filter(order -> orderContainsText(order, searchText))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "orders", key = "{#brandName}")
    public List<Order> getOrderByBrandName(String brandName) {
        List<Order> orders = orderRepository.findAllByBrandName(brandName);

        return orders.stream()
                .filter(order -> order.getCreatedAt().getMonth().name().equals(LocalDate.now().getMonth().name()))
                .peek(order -> {
                    order.setBrandName(order.getBrandName().substring(0, 3));
                    String name = order.getName() == null ? "Luxury Brand"
                            : String.format("Luxury %s %s", order.getName(), order.getBrandName());
                    order.setName(name);
                }).collect(Collectors.toList());
    }

    @Override
    public List<Order> getOrderByBrandNameAndName(String brandName, String name) {
        return orderRepository.findAllByBrandNameAndName(brandName, name);
    }

    private static List<OrderDto> getCachedSearchResult(List<Object> cachedOrders) {
        return cachedOrders.stream()
                .filter(cachedOrder -> cachedOrder instanceof OrderDto)
                .map(cachedOrder -> (OrderDto) cachedOrder)
                .collect(Collectors.toList());
    }

    private boolean orderContainsText(OrderDto orderDto, String searchText) {
        if (orderDto == null || searchText == null) {
            return false; // Return false if either orderDto or searchText is null
        }

        String id = orderDto.getId() != null ? orderDto.getId().toString() : EMPTY_STRING;
        String brandName = orderDto.getBrandName() != null ? orderDto.getBrandName() : EMPTY_STRING;
        String description = orderDto.getDescription() != null ? orderDto.getDescription() : EMPTY_STRING;
        String name = orderDto.getName() != null ? orderDto.getName() : EMPTY_STRING;

        return id.contains(searchText) ||
                brandName.contains(searchText) ||
                description.contains(searchText) ||
                name.contains(searchText);
    }
}
