package com.daniyal.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="books",
        indexes = {
            @Index(name="idx_book_title", columnList = "title"),
            @Index(name="idx_book_genre", columnList = "genre"),
            @Index(name="idx_book_isbn", columnList = "isbn")

        }
)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  title;
    //private String author;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="book_author",
            joinColumns=@JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="author_id")
            )
    @Builder.Default
    private Set<Author> authors=new HashSet<>();

    private String genre;

    @Column(unique=true)
    private String isbn;

    private String description;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;
    @OneToMany(mappedBy = "book")
    @JsonIgnore // checkout day 8 json-serilizatio-issue.md why this is necessary but currently not ir my case
    private List<OrderItem> orderItems;


}
