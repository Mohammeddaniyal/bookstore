package com.daniyal.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  title;
    private String author;
    @Column(unique=true)
    private String isbn;

    private String description;
    private BigDecimal price;
    private int quantity;
}
