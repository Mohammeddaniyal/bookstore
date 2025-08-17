package com.daniyal.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="authors",
        indexes = {
            @Index(name="idx_author_name", columnList = "name")
        }
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    // Optional bidirectional mapping; required only if i need to fetch books of an author
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;
}
