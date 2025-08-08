package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.AuthorRequestDTO;
import com.daniyal.bookstore.dto.AuthorResponseDTO;
import com.daniyal.bookstore.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;


    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO authorRequest)
    {
        return new ResponseEntity<>(authorService.createAuthor(authorRequest),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors()
    {
        return new ResponseEntity<>(authorService.getAllAuthors(),HttpStatus.OK);
    }
}
