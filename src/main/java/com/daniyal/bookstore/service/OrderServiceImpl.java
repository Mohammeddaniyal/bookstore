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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        /* ✅ Optimization:
 Instead of calling bookRepository.findById(...) inside the loop for each order item
 (which causes N separate SQL queries → N+1 problem),
 we collect all required book IDs first and fetch them in a single query using findAllById().
 This reduces database round-trips from O(N) to O(1) for book lookups,
 making order placement significantly faster for orders with many items.
*/

        // fetch all book ID's from order request all at once
        List<Long> bookIds=orderRequest.getOrderItems().stream()
                .map(OrderItemRequestDTO::getBookId)
                .collect(Collectors.toList());

        // Single query to fetch all books
        List<Book> books=bookRepository.findAllById(bookIds);

        Map<Long,Book> bookMap=books.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));

        // prepare OrderItem, validate each book and quantity
        List<OrderItem> orderItems=new ArrayList<>();
        BigDecimal totalAmount=BigDecimal.ZERO;
        List<OrderItemRequestDTO> requestOrderItems=orderRequest.getOrderItems();
        for(OrderItemRequestDTO requestOrderItem:requestOrderItems)
        {

            Book book=bookMap.get(requestOrderItem.getBookId());
            if(book==null) {
                throw new BookNotFoundException("Book not found with id " + requestOrderItem.getBookId());
            }
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
        // Optimized query: fetches Order by ID along with its OrderItems and related Books in one SQL statement
        // Prevents N+1 query problem and ensures all details needed for the response DTO are loaded eagerly

        Order order= orderRepository.findByIdWithItemsAndBooks(orderId)
                .orElseThrow(()->(new OrderNotFoundException("Order not found")));
        // Ownership check — hide existence from non-owner by returning 404
        if(!isAdmin && !order.getUser().getEmail().equals(email))
        {
            // Security decision: For non-admin users, treat orders they don’t own as "not found" (404)
            // to prevent leaking the existence of other users’ orders.
            throw new OrderNotFoundException("Order not found");
        }
        // At this point Order, OrderItems, and Books will be lazily loaded
        // but are safe to access inside @Transactional in toOrderResponseDTO
        return toOrderResponseDTO(order);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> listOrdersForUser(String targetEmail,String loggedInEmail,boolean isAdmin) {
        /*
         Service method to fetch all orders for a given user.
 - If caller is ADMIN, can view orders for any specified email.
 - If caller is not an admin, they can only view orders for their own email.
 - Uses a repository method with JOIN FETCH to load OrderItems and Books in one query,
   avoiding the N+1 select problem.
 - Throws no explicit access-denied error here because email filtering via parameters
   already ensures correct ownership visibility.
        */
        String emailToQuery=isAdmin?targetEmail:loggedInEmail;
        List<Order> orders=orderRepository.findAllByUserEmailWithItemsAndBooks(emailToQuery);
        return orders.stream()
                .map(this::toOrderResponseDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> listAllOrders() {
        return orderRepository.findAllWithItemsAndBooks().stream()
                .map(this::toOrderResponseDTO)
                .toList();
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
