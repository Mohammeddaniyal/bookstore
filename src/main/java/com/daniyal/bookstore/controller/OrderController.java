package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.OrderRequestDTO;
import com.daniyal.bookstore.dto.OrderResponseDTO;
import com.daniyal.bookstore.enums.OrderStatus;
import com.daniyal.bookstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) {
        // get username from authentication
        // because as username we have setted the email not user actual username
        String email = authentication.getName();
        return new ResponseEntity<>(orderService.placeOrder(orderRequestDTO, email), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return new ResponseEntity<>(orderService.getOrderById(id, email, isAdmin), HttpStatus.FOUND);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDTO>> getAllMyOrders(Authentication authentication) {
        /*
         Controller endpoint for fetching the currently logged-in user's own orders.
 - Extracts email from Authentication object for logged-in user.
 - Calls service to retrieve only the orders belonging to this email.
 - Admins calling this endpoint will also only see their own orders.
 - Uses secure email binding: users cannot pass in someone else’s email here.
         */
        String email = authentication.getName();
        return ResponseEntity.ok(orderService.listOrdersForUser(email, email, false));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getOrderForUser(@RequestParam String email, Authentication authentication) {
        /*
         Controller endpoint for ADMIN to fetch orders for a specific user by email.
 - Requires ADMIN authority (@PreAuthorize ensures only admins can reach here).
 - Admin provides target user's email in the path variable.
 - Passes both the target email and admin's own email to service.
 - Non-admins cannot access this endpoint (security handled at method level).
         */
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(orderService.listOrdersForUser(email, authentication.getName(), isAdmin));

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        /*
         Controller endpoint to fetch all orders in the system.
 - Accessible only to ADMIN users (secured via @PreAuthorize).
 - Delegates to service layer for fetching with JOIN FETCH optimization.
 - Returns list of OrderResponseDTO containing full order details.
         */
        return ResponseEntity.ok(orderService.listAllOrders());
    }

    @PostMapping("/{orderId}/cancel/")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, Authentication authentication) {
        // Any user can cancel their own pending orders; admin can cancel any
        /*
         Cancels an order by ID.
 - Non-admin users can only cancel their own orders that are still PENDING.
 - Admins can cancel any order.
 - Restores stock quantities after cancellation.
 - Returns 204 No Content on success.
         */
        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        orderService.cancelOrder(orderId, email, isAdmin);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    // ADMIN-only: update status of any order
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus) {
    /*
     ADMIN-only: Updates the status of an order (e.g., PENDING -> SHIPPED).
 - Access restricted via @PreAuthorize("hasAuthority('ADMIN')").
 - Accepts new status as request parameter.
 - Returns 204 No Content on success.
     */    orderService.updateOrderStatus(orderId,orderStatus);
        return ResponseEntity.noContent().build();
    }
}
