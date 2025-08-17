package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payment request data")
public class PaymentRequestDTO {
    @NotNull(message = "Order ID cannot be null")
    @Positive(message = "Order ID cannot be negative or zero")
    @Schema(description = "ID of the order to pay", example = "3", required = true)
    private Long orderId;
    @NotNull(message = "Amount is required")
    @Positive(message="Amount cannot be negative or zero")
    @Schema(description = "Payment amount", example = "99.99", required = true)
    private BigDecimal amount; // for future real payments
    // later i can add fields like cardNumber, expiryDate, etc. when going real
}
