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
@Table(name="order_items",
        indexes = {
            @Index(name="idx_order_item_order", columnList = "order_id"),
            @Index(name="idx_order_item_book", columnList = "book_id")
        }
)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name="book_id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id")
    private Order order;
    private int quantity;
    private BigDecimal subTotal;
}
