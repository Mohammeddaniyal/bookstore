package com.daniyal.bookstore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@Builder
public class BookUpdateDTO {
    private String title;
    private Set<Long> authorIds;
    private String genre;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
}
