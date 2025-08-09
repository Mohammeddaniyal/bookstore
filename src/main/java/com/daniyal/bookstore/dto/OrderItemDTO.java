package com.daniyal.bookstore.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

        @NotNull(message = "Book ID cannot be null")
        private Long bookId;

        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;
}
