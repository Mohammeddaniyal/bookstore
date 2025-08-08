package com.daniyal.bookstore.dto;

import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    @Positive(message = "The price must be positive")
    private BigDecimal price;
    @PositiveOrZero(message = "The quantity cannot be negative")
    private Integer quantity;
    private String imageUrl;
}
