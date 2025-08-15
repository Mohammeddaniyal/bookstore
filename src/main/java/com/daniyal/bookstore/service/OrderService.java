package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.OrderRequestDTO;
import com.daniyal.bookstore.dto.OrderResponseDTO;
import com.daniyal.bookstore.enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDTO placeOrder(OrderRequestDTO dto, String email);
    OrderResponseDTO getOrderById(Long orderId, String email, boolean isAdmin);
    List<OrderResponseDTO> listOrdersForUser(String targetEmail,String loggedInEmail,boolean isAdmin);
    List<OrderResponseDTO> listAllOrders();

    Page<OrderResponseDTO> listAllOrders(Pageable pageable);


    Page<OrderResponseDTO> filterOrders(
            OrderStatus status, String email, Pageable pageable);

    void cancelOrder(Long orderId, String email, boolean isAdmin);
    void updateOrderStatus(Long orderId, OrderStatus status);
}

