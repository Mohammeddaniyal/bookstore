package com.daniyal.bookstore.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false)
    @JoinColumn(name="book_id")
    private Book book;
    @ManyToOne(optional = false)
    @JoinColumn(name="order_id")
    private Order order;
    private int quantity;
    private BigDecimal subTotal;
}
