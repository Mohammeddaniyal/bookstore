package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.AuthorRequestDTO;
import com.daniyal.bookstore.dto.AuthorResponseDTO;

import java.util.List;

public interface AuthorService {
    AuthorResponseDTO createAuthor(AuthorRequestDTO request);
    List<AuthorResponseDTO> getAllAuthors();
}
