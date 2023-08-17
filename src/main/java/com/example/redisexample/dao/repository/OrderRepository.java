package com.example.redisexample.dao.repository;

import com.example.redisexample.dao.entity.Order;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // This query just written for show Java 17 Query looking
    @Query("""
            SELECT order FROM Order order
            WHERE order.id = :orderId AND order.isDeleted = false
            """)
    Optional<Order> findOrderById(Long orderId);

    List<Order> findAllByBrandName(String brandName);

    @Cacheable(value = "orderByBrandNameAndName", key = "{#brandName, #name}")
    List<Order> findAllByBrandNameAndName(String brandName, String name);


//    @Override
//    @CachePut(value = "orderById", key = "#p0.id")
//    <S extends Order> S save(@NonNull S entity);

}
