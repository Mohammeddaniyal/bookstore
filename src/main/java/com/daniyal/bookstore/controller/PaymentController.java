package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.PaymentRequestDTO;
import com.daniyal.bookstore.dto.PaymentResponseDTO;
import com.daniyal.bookstore.exceptions.ApiErrorResponse;
import com.daniyal.bookstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment", description = "Endpoints for payment processing")
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment request or order already paid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponseDTO> payForOrder(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        PaymentResponseDTO response = paymentService.processPayment(paymentRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
