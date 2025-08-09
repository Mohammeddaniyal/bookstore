package com.daniyal.bookstore.repository;

import com.daniyal.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
public List<Order> findAllByUser_Username(String username);
}
