package com.daniyal.bookstore.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class BookRequestDTO {
    @NotBlank(message="Title is required")
    private String title;

    @NotEmpty(message = "Author IDs are required.")
    private Set<Long> authorIds;
    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message="ISBN is required")
    private String isbn;
    @Size(max=500, message = "Description can't be more than 500 characters")
    private String description;
    @NotNull(message = "Price is required")
    @Positive(message="Price must be greater than zero")
    private BigDecimal price;
    @Min(value = 0,message="Quanitity cannot be negative")
    private int quantity;
    @URL
    private String imageUrl;
}
