package com.daniyal.bookstore.repository;

import com.daniyal.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    public List<Order> findAllByUser_Username(String username);
    @Query("SELECT o FROM Order o" +
            "JOIN FETCH o.orderItems oi" +
            "JOIN FETCH oi.book WHERE o.id=:orderId")
    Optional<Order> findByIdWithItemsAndBooks(@Param("orderId")Long Id);

}
