package com.daniyal.bookstore.service;


import com.daniyal.bookstore.dto.AuthorRequestDTO;
import com.daniyal.bookstore.dto.AuthorResponseDTO;
import com.daniyal.bookstore.entity.Author;
import com.daniyal.bookstore.exceptions.AuthorAlreadyExistsException;
import com.daniyal.bookstore.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public AuthorResponseDTO createAuthor(AuthorRequestDTO request) {
       if(authorRepository.existsByNameIgnoreCase(request.getName()))
       {
           throw new AuthorAlreadyExistsException("Author already exists.");
       }
       Author author= Author.builder()
               .name(request.getName())
               .build();
       Author savedAuthor=authorRepository.save(author);
       return AuthorResponseDTO.builder()
               .name(savedAuthor.getName())
               .id(savedAuthor.getId())
               .build();
    }

    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> AuthorResponseDTO.builder()
                        .id(author.getId())
                        .name(author.getName())
                        .build())
                .toList();
    }
}
