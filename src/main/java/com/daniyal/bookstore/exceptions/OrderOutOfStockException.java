package com.daniyal.bookstore.exceptions;

public class OrderOutOfStockException extends RuntimeException {
    public OrderOutOfStockException(String message) {
        super(message);
    }
}
