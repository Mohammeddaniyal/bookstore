package com.daniyal.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_items")
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
