package com.daniyal.bookstore.dto;
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
public class OrderItemRequestDTO {

        @NotNull(message = "Book ID cannot be null")
        @Positive(message = "Book ID cannot be negative or zero")
        private Long bookId;

        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;
}
