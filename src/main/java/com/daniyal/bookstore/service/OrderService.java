package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.OrderRequestDTO;
import com.daniyal.bookstore.dto.OrderResponseDTO;
import com.daniyal.bookstore.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDTO placeOrder(OrderRequestDTO dto, String email);
    OrderResponseDTO getOrderById(Long orderId, String email, boolean isAdmin);
    List<OrderResponseDTO> listOrdersForUser(String email);
    List<OrderResponseDTO> listAllOrders();
    void cancelOrder(Long orderId, String email, boolean isAdmin);
    void updateOrderStatus(Long orderId, OrderStatus status);
}

