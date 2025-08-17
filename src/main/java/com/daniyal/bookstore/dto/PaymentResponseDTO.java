package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response after payment processing")
public class PaymentResponseDTO {
    @Schema(description = "Indicates whether payment succeeded", example = "true")
    private boolean success;

    @Schema(description = "Message describing the payment result", example = "Mock payment successful")
    private String message;

    @Schema(description = "ID of the order related to payment", example = "1001")
    private Long orderId;
}
