package com.daniyal.bookstore.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Individual item detail in an order response")
public class OrderItemResponseDTO {
        @Schema(description = "ID of the book", example = "10")
        private Long bookId;

        @Schema(description = "Title of the book", example = "Clean Architecture")
        private String bookTitle;

        @Schema(description = "Quantity ordered", example = "2")
        private int quantity;

        @Schema(description = "Subtotal for the item (price * quantity)", example = "100.00")
        private BigDecimal subTotal;
}
