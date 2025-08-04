package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.BookRequestDTO;
import com.daniyal.bookstore.dto.BookResponseDTO;
import com.daniyal.bookstore.dto.BookUpdateDTO;
import com.daniyal.bookstore.entity.Book;
import com.daniyal.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @PostMapping
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookRequestDTO bookRequest)
    {
        return new ResponseEntity<>(bookService.createBook(bookRequest),HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> partialUpdateBook(@RequestBody BookUpdateDTO bookRequest, @PathVariable Long id)
    {
        return new ResponseEntity<>(bookService.partialUpdateBook(id,bookRequest),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> fullUpdateBook(@PathVariable Long id,@Valid @RequestBody BookRequestDTO bookRequest)
    {
        return new ResponseEntity<>(bookService.fullUpdateBook(id,bookRequest),HttpStatus.OK);
    }
}
