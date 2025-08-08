package com.daniyal.bookstore.repository;

import com.daniyal.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByIsbn(String isbn);
   // Optional<Book> findByAuthorAndTitle(String author,String title);
    List<Book> findByTitle(String title);
    @Query("SELECT DISTINCT b from Book b LEFT JOIN FETCH b.authors WHERE b.title=:title")
    List<Book> findByTitleWithAuthors(@Param("title") String title);

}
