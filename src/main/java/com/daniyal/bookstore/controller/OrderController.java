package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.OrderRequestDTO;
import com.daniyal.bookstore.dto.OrderResponseDTO;
import com.daniyal.bookstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication)
    {
        // get username from authentication
        // because as username we have setted the email not user actual username
        String email = authentication.getName();
        return new ResponseEntity<>(orderService.placeOrder(orderRequestDTO,email), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id,Authentication authentication)
    {
        String email=authentication.getName();
        boolean isAdmin=authentication.getAuthorities()
                .stream()
                .anyMatch(a->a.getAuthority().equals("ADMIN"));
        System.out.println("Is admin : "+isAdmin);
        return new ResponseEntity<>(orderService.getOrderById(id,email,isAdmin),HttpStatus.FOUND);
    }


}
