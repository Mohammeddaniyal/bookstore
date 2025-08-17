package com.daniyal.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    @NotNull(message = "Order ID cannot be null")
    @Positive(message = "Order ID cannot be negative or zero")
    private Long orderId;
    @NotNull(message = "Amount is required")
    @Positive(message="Amount cannot be negative or zero")
    private BigDecimal amount; // for future real payments
    // later i can add fields like cardNumber, expiryDate, etc. when going real
}
