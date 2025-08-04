package com.daniyal.bookstore.exceptions;

public class BookNotExistsException extends RuntimeException {
    public BookNotExistsException(String message) {
        super(message);
    }
}
