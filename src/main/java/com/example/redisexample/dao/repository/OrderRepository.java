package com.example.redisexample.dao.repository;

import com.example.redisexample.dao.entity.Order;
import lombok.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Cacheable(value = "orderById")
    @Query("""
            SELECT order FROM Order order
            WHERE order.id = :orderId AND order.isDeleted = false
            """)
    Optional<Order> findOrderById(Long orderId);

    @Override
    @Cacheable(value = "orders")
    @Query(""" 
            SELECT o from Order o""")
    List<Order> findAll();

    @Cacheable(value = "orderByBrandName", key = "{#brandName}")
    List<Order> findAllByBrandName(String brandName);

    @Cacheable(value = "orderByBrandNameAndName", key = "{#brandName, #name}")
    List<Order> findAllByBrandNameAndName(String brandName, String name);

    @Override
    <S extends Order> S save(@NonNull S entity);

}
