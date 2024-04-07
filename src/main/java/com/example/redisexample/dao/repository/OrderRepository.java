package com.example.redisexample.dao.repository;

import com.example.redisexample.dao.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT order FROM Order order
            WHERE order.id = :orderId AND order.isDeleted = false
            """)
    Optional<Order> findOrderById(Long orderId);

    List<Order> findAllByBrandName(String brandName);

    @Cacheable(value = "orderByBrandNameAndName", key = "{#brandName, #name}")
    List<Order> findAllByBrandNameAndName(String brandName, String name);

    //TODO: This is the safest version to keep cache clean, it keeps cache up to date when u save each record
    //@Override
    //@CachePut(value = "orderById", key = "#p0.id")
    //<S extends Order> S save(@NonNull S entity);

}
