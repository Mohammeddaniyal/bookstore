package com.daniyal.bookstore.repository;

import com.daniyal.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findByAuthorAndTitle(String author,String title);
    List<Book> findByTitle(String title);
}
