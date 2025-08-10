package com.daniyal.bookstore.dto;

import com.daniyal.bookstore.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateDTO {

    @NotNull(message = "Status must not be null")
    private OrderStatus status;
}
