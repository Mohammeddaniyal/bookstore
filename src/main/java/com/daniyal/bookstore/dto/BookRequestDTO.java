package com.daniyal.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class BookRequestDTO {
    @NotBlank(message="Title is required")
    private String title;
    @NotBlank(message="Author is required")
    private String author;
    @NotBlank(message="ISBN s required")
    private String isbn;

    private String description;
    @NotNull(message = "Price is required")
    @Positive(message="Price must be greater than zero")
    private BigDecimal price;
    @Min(value = 0,message="Quanitity cannot be negative")
    private int quantity;
}
