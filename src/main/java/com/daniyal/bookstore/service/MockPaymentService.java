package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.PaymentRequestDTO;
import com.daniyal.bookstore.dto.PaymentResponseDTO;
import com.daniyal.bookstore.entity.Order;
import com.daniyal.bookstore.enums.PaymentStatus;
import com.daniyal.bookstore.exceptions.OrderNotFoundException;
import com.daniyal.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MockPaymentService implements PaymentService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getPaymentStatus() != PaymentStatus.UNPAID) {
            return PaymentResponseDTO.builder()
                    .success(false)
                    .message("Order already paid or refunded")
                    .orderId(order.getId())
                    .build();
        }

        // Mock payment always succeeds
        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);

        return PaymentResponseDTO.builder()
                .success(true)
                .message("Mock payment successful")
                .orderId(order.getId())
                .build();
    }
}
