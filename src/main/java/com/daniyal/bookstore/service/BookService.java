package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.BookRequestDTO;
import com.daniyal.bookstore.dto.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {
    BookResponseDTO createBook(BookRequestDTO bookRequest);
    Optional<BookResponseDTO> getBookById(Long id);
    Page<BookResponseDTO> getAllBooks(Pageable pageable);
    BookResponseDTO updateBook(Long id, BookRequestDTO bookRequest);
    void deleteBook(Long id);
}
