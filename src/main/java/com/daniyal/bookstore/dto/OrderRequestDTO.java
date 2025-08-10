package com.daniyal.bookstore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotNull(message = "Order must contain at least one item")
    @NotEmpty(message = "Order items are required")
    @Valid // <-- this ensures OrderItemDto inside items list are validated
    private List<OrderItemRequestDTO> orderItems;
}
