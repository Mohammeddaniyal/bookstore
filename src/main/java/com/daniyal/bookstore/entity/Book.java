package com.daniyal.bookstore.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
    private Long id;

    private String  title;
    private String author;
    private String isbn;

    private String description;
    private BigDecimal price;
    private int quantity;
}
