package com.daniyal.bookstore.dto;

import com.daniyal.bookstore.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Request DTO to update order status")
public class OrderStatusUpdateDTO {

    @NotNull(message = "Status must not be null")
    @Schema(description = "New order status to set", example = "SHIPPED", required = true)
    private OrderStatus status;
}
