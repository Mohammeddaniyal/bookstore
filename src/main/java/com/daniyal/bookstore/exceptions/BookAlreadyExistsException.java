package com.daniyal.bookstore.exceptions;

public class BookAlreadyExistsException extends RuntimeException {
  public BookAlreadyExistsException(String message) {
    super(message);
  }
}
