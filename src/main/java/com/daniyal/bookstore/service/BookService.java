package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.BookRequestDTO;
import com.daniyal.bookstore.dto.BookResponseDTO;
import com.daniyal.bookstore.dto.BookUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookResponseDTO createBook(BookRequestDTO bookRequest);
    BookResponseDTO getBookById(Long id);
    List<BookResponseDTO> getAllBooks();
    Page<BookResponseDTO> getAllBooks(Pageable pageable);
    BookResponseDTO partialUpdateBook(Long id, BookUpdateDTO bookRequest);
    BookResponseDTO fullUpdateBook(Long id,BookRequestDTO bookRequest);
    void deleteBook(Long id);
}
