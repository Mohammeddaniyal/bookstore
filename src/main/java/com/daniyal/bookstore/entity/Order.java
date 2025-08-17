package com.daniyal.bookstore.entity;

import com.daniyal.bookstore.enums.OrderStatus;
import com.daniyal.bookstore.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders",
        indexes ={
            @Index(name="idx_order_user", columnList = "user_id"),
            @Index(name="idx_order_status", columnList = "order_status"),
            @Index(name="idx_order_payment_status", columnList = "payment_status"),
            @Index(name="idx_order_createdAt", columnList = "created_at")
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems=new ArrayList<>();

    private BigDecimal totalAmount;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // there are three ways to assign date
    // 1 handle manually in service class
    // 2 Using @PrePersist and @PreUpdate
    // 3 Using jpa auditing, checkout docs/day8/jpa-auditing-vs-prepersist.md

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
