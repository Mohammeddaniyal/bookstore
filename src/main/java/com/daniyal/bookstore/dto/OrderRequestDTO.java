package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Order request containing list of order items")
public class OrderRequestDTO {

    @NotNull(message = "Order must contain at least one item")
    @NotEmpty(message = "Order items are required")
    @Valid // <-- this ensures OrderItemDto inside items list are validated
    @Schema(description = "List of order items", required = true)
    private List<OrderItemRequestDTO> orderItems;
}
