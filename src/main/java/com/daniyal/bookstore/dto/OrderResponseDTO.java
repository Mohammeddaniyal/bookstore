package com.daniyal.bookstore.dto;

import com.daniyal.bookstore.enums.OrderStatus;
import com.daniyal.bookstore.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private List<OrderItemResponseDTO> orderItems;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String createdAt;
    private String updatedAt;
    private BigDecimal totalAmount;
}
