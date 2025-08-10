package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.OrderItemRequestDTO;
import com.daniyal.bookstore.dto.OrderItemResponseDTO;
import com.daniyal.bookstore.dto.OrderRequestDTO;
import com.daniyal.bookstore.dto.OrderResponseDTO;
import com.daniyal.bookstore.entity.Book;
import com.daniyal.bookstore.entity.Order;
import com.daniyal.bookstore.entity.OrderItem;
import com.daniyal.bookstore.entity.User;
import com.daniyal.bookstore.enums.OrderStatus;
import com.daniyal.bookstore.exceptions.*;
import com.daniyal.bookstore.repository.BookRepository;
import com.daniyal.bookstore.repository.OrderRepository;
import com.daniyal.bookstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;



    @Override
    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequest, String email) {

        // lookup user

        User user=userRepository.findByEmail(email).
                orElseThrow(()-> new UserNotFoundException("User not found"));

        // prepare OrderItem, validate each book and quantity
        List<OrderItem> orderItems=new ArrayList<>();
        BigDecimal totalAmount=BigDecimal.ZERO;
        List<OrderItemRequestDTO> requestOrderItems=orderRequest.getOrderItems();
        for(OrderItemRequestDTO requestOrderItem:requestOrderItems)
        {
            Book book=bookRepository.findById(requestOrderItem.getBookId())
                            .orElseThrow(()-> new BookNotFoundException("Book not found "+requestOrderItem.getBookId()));

            if(book.getQuantity()<requestOrderItem.getQuantity())
            {
                throw new OrderOutOfStockException("Not enough stock for book : "+book.getTitle());
            }

            book.setQuantity(book.getQuantity()-requestOrderItem.getQuantity());

            BigDecimal itemSubTotal=book.getPrice().multiply(BigDecimal.valueOf(requestOrderItem.getQuantity()));

            OrderItem orderItem=OrderItem.builder()
                    .book(book)
                    .quantity(requestOrderItem.getQuantity())
                    .subTotal(itemSubTotal)
                    .build();
            orderItems.add(orderItem);
            totalAmount=totalAmount.add(itemSubTotal);
        }

        // create OrderEntity link item & user
        Order order=Order.builder()
                .user(user)
                .orderItems(orderItems)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .build();
        orderItems.forEach(item->item.setOrder(order));

        // persist order (cascade items)
         Order savedOrder=orderRepository.save(order);

         return toOrderResponseDTO(savedOrder);

    }


    @Override
    @Transactional
    public OrderResponseDTO getOrderById(Long orderId, String email, boolean isAdmin) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->(new UserNotFoundException("User not found")));
        if(!isAdmin && !user.getEmail().equals(email))
        {
            throw new AccessDeniedException("Access denied to order id : "+orderId);
        }
        Order order= orderRepository.findById(orderId)
                .orElseThrow(()->(new OrderNotFoundException("Order not found")));
        return toOrderResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> listOrdersForUser(String username) {
        return List.of();
    }

    @Override
    public List<OrderResponseDTO> listAllOrders() {
        return List.of();
    }

    @Override
    public void cancelOrder(Long orderId, String username, boolean isAdmin) {

    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {

    }

    private OrderResponseDTO toOrderResponseDTO(Order order)
    {
        List<OrderItemResponseDTO> orderItems=order.getOrderItems().stream()
                .map(item-> OrderItemResponseDTO.builder()
                        .bookId(item.getBook().getId())
                        .bookTitle(item.getBook().getTitle())
                        .subTotal(item.getSubTotal())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        // format dates as string
        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderItems(orderItems)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt() != null
                        ? DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(order.getCreatedAt())
                        : null)
                .updatedAt(order.getUpdatedAt() != null
                        ? DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(order.getUpdatedAt())
                        : null)
                .build();
    }

}
