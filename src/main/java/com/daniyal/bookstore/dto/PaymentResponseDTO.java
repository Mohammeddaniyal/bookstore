package com.daniyal.bookstore.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private boolean success;
    private String message;
    private Long orderId;
}
