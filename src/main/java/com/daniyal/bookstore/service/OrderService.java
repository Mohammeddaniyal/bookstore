package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.OrderRequestDTO;
import com.daniyal.bookstore.dto.OrderResponseDTO;
import com.daniyal.bookstore.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDTO placeOrder(OrderRequestDTO dto, String username);
    OrderResponseDTO getOrderById(Long orderId, String username, boolean isAdmin);
    List<OrderResponseDTO> listOrdersForUser(String username);
    List<OrderResponseDTO> listAllOrders();
    void cancelOrder(Long orderId, String username, boolean isAdmin);
    void updateOrderStatus(Long orderId, OrderStatus status);
}

