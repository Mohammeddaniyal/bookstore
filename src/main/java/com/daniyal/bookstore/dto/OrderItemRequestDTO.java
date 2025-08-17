package com.daniyal.bookstore.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "An item in an order request")
public class OrderItemRequestDTO {

        @NotNull(message = "Book ID cannot be null")
        @Positive(message = "Book ID cannot be negative or zero")
        @Schema(description = "ID of the book ordered", example = "15", required = true)
        private Long bookId;

        @Min(value = 1, message = "Quantity must be at least 1")
        @Schema(description = "Quantity of the book to order", example = "3", required = true)
        private int quantity;
}
