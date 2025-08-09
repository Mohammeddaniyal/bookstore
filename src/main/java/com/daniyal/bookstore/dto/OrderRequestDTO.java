package com.daniyal.bookstore.dto;

import com.daniyal.bookstore.entity.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequestDTO {

    @NotNull(message = "Order must contain at least one item")
    @NotEmpty(message = "Order items are required")
    @Valid // <-- this ensures OrderItemDto inside items list are validated
    private List<OrderItemDTO> items;
}
