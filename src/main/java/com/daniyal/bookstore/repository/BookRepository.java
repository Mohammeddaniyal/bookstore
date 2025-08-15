package com.daniyal.bookstore.repository;

import com.daniyal.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
       SELECT DISTINCT b
       FROM Book b
       LEFT JOIN b.authors a
       WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
         AND (:author IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%')))
         AND (:genre IS NULL OR LOWER(b.genre) = LOWER(:genre))
       """,
            countQuery = """
       SELECT COUNT(DISTINCT b)
       FROM Book b
       LEFT JOIN b.authors a
       WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
         AND (:author IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%')))
         AND (:genre IS NULL OR LOWER(b.genre) = LOWER(:genre))
       """)
    Page<Book> searchBooksMultiAuthor(@Param("title") String title,
                                      @Param("author") String author,
                                      @Param("genre") String genre,
                                      Pageable pageable);

}
