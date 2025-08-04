package com.daniyal.bookstore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class BookUpdateDTO {
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private Integer quantity;
}
