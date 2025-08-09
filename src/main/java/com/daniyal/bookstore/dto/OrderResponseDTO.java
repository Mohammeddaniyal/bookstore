package com.daniyal.bookstore.dto;

import com.daniyal.bookstore.enums.OrderStatus;
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
    private OrderStatus status;
    private String createdAt;
    private String updatedAt;
    private BigDecimal totalAmount;
}
