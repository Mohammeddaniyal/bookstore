package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.BookRequestDTO;
import com.daniyal.bookstore.dto.BookResponseDTO;
import com.daniyal.bookstore.dto.BookUpdateDTO;
import com.daniyal.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<BookResponseDTO> partialUpdateBook(@Valid @RequestBody BookUpdateDTO bookRequest, @PathVariable Long id)
    {
        return new ResponseEntity<>(bookService.partialUpdateBook(id,bookRequest),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> fullUpdateBook(@PathVariable Long id,@Valid @RequestBody BookRequestDTO bookRequest)
    {
        return new ResponseEntity<>(bookService.fullUpdateBook(id,bookRequest),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id)
    {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBook(@PathVariable Long id)
    {
        return new ResponseEntity<>(bookService.getBookById(id),HttpStatus.FOUND);
    }
    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> getAllBooks(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy));
        Page<BookResponseDTO> books=bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        // clamp size to avoid abuse
        int safeSize = Math.min(Math.max(size, 1), 100);

        Sort sort = sortDir.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, safeSize, sort);

        return ResponseEntity.ok(bookService.searchBooks(title, author, genre, pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks()
    {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
}
