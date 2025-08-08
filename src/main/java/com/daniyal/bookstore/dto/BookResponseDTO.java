package com.daniyal.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@Builder
public class BookResponseDTO {
    private Long id;
    private String title;
    private Set<String> authors;
    private String genre;
    private String isbn;
    private String description;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;
}
