package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.OrderRequestDTO;
import com.daniyal.bookstore.dto.OrderResponseDTO;
import com.daniyal.bookstore.enums.OrderStatus;
import com.daniyal.bookstore.enums.PaymentStatus;
import com.daniyal.bookstore.exceptions.ApiErrorResponse;
import com.daniyal.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "APIs for order management including placing, updating, and retrieving orders")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Place a new order",
            description = "Creates an order for the authenticated user with given order items.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or out-of-stock",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "One or more books not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
            })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) {
        // get username from authentication
        // because as username we have setted the email not user actual username
        String email = authentication.getName();
        return new ResponseEntity<>(orderService.placeOrder(orderRequestDTO, email), HttpStatus.CREATED);
    }

    @Operation(summary = "Get order by ID",
            description = "Retrieve details of an order by its ID. Non-admins can access only their own orders.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found or access denied",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return new ResponseEntity<>(orderService.getOrderById(id, email, isAdmin), HttpStatus.FOUND);
    }

    @Operation(summary = "Get all orders of the logged-in user",
            description = "Fetches all orders placed by the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of user's orders retrieved",
                    content = @Content(
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
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

    @Operation(summary = "Get all orders for a specific user (ADMIN only)",
            description = "Admins can query orders for any user by email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved for specified user",
                    content = @Content(
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Admin access required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
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

    @Operation(summary = "Get all orders in the system (ADMIN only)",
            description = "Admins can retrieve all orders with details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All orders retrieved",
                    content = @Content(
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Admin access required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/getAll")
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



    @Operation(summary = "Get paginated orders (ADMIN only)",
            description = "Retrieve paginated list of orders with sorting.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated orders retrieved",
                    content = @Content(
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Admin access required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy));
        Page<OrderResponseDTO> orders=orderService.listAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }


    @Operation(summary = "Search orders with filters (ADMIN only)",
            description = "Search orders by status, payment status, and user email with pagination and sorting.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered orders retrieved",
                    content = @Content(
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Admin access required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrdersByAppliedFilters(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir)
    {

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortDir.equalsIgnoreCase("ASC") ?
                        Sort.by(sortBy).ascending() :
                        Sort.by(sortBy).descending()
        );

        Page<OrderResponseDTO> result = orderService.filterOrders(orderStatus,paymentStatus, email, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Cancel an order",
            description = "Cancel a pending order. Users can only cancel their own pending orders; admins can cancel any.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order successfully cancelled"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found or access denied",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, Authentication authentication)
    {
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

    @Operation(summary = "Update status of an order (ADMIN only)",
            description = "Admins can update the order status, e.g., from PENDING to SHIPPED.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status or order state",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Admin access required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    // ADMIN-only: update status of any order
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestParam("orderStatus") OrderStatus orderStatus) {
    /*
     ADMIN-only: Updates the status of an order (e.g., PENDING -> SHIPPED).
 - Access restricted via @PreAuthorize("hasAuthority('ADMIN')").
 - Accepts new status as request parameter.
 - Returns 204 No Content on success.
     */    orderService.updateOrderStatus(orderId,orderStatus);
        return ResponseEntity.noContent().build();
    }
}
