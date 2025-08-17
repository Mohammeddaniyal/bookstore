package com.daniyal.bookstore.dto;

import com.daniyal.bookstore.enums.OrderStatus;
import com.daniyal.bookstore.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response returned after placing or fetching an order")
public class OrderResponseDTO {

    @Schema(description = "Unique identifier of the order", example = "1001")
    private Long id;

    @Schema(description = "List of order items")
    private List<OrderItemResponseDTO> orderItems;

    @Schema(description = "Current status of the order", example = "PENDING")
    private OrderStatus orderStatus;

    @Schema(description = "Current payment status", example = "UNPAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Timestamp when order was created", example = "2025-08-17T16:46:13.7074082")
    private String createdAt;

    @Schema(description = "Timestamp when order was last updated", example = "2025-08-17T16:46:13.7074082")
    private String updatedAt;

    @Schema(description = "Total amount for the order", example = "150.00")
    private BigDecimal totalAmount;
}
