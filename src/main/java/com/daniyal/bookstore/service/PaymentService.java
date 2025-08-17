package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.PaymentRequestDTO;
import com.daniyal.bookstore.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest);
}
