package com.daniyal.bookstore.entity;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class OrderItem {
    private Long id;
    private Book book;
    private Order order;
    private int quantity;
    private BigDecimal subTotal;
}
