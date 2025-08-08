package com.daniyal.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  title;
    //private String author;
    @ManyToMany
    private Set<Author> authors;

    @Column(unique=true)
    private String isbn;

    private String description;
    private BigDecimal price;
    private int quantity;

    @OneToMany(mappedBy = "book")
    private List<OrderItem> orderItems;


}
