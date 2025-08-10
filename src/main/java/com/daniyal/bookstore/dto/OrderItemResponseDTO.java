package com.daniyal.bookstore.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {

        private Long bookId;
        private String bookTitle;
        private int quantity;
        private BigDecimal subTotal;

}
