package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Schema(description = "Request DTO for creating or updating a book")
public class BookRequestDTO {
    @NotBlank(message="Title is required")
    @Schema(description = "Title of the book", example = "Clean Code", required = true)
    private String title;

    @NotEmpty(message = "Author IDs are required.")
    @Schema(description = "Set of author IDs related to the book", example = "[1, 2]", required = true)
    private Set<Long> authorIds;

    @NotBlank(message = "Genre is required")
    @Schema(description = "Genre or category of the book", example = "Programming", required = true)
    private String genre;

    @NotBlank(message="ISBN is required")
    @Schema(description = "ISBN number of the book", example = "978-0132350884", required = true)
    private String isbn;
    @Size(max=500, message = "Description can't be more than 500 characters")
    @Schema(description = "Brief description of the book", example = "A handbook of agile software craftsmanship")
    private String description;
    @NotNull(message = "Price is required")
    @Positive(message="Price must be greater than zero")
    @Schema(description = "Price of the book", example = "39.99", required = true)
    private BigDecimal price;

    @Min(value = 0,message="Quanitity cannot be negative")
    @Schema(description = "Available quantity in stock", example = "10")
    private int quantity;

    @URL
    @Schema(description = "URL to book's cover image", example = "http://example.com/cleancode.jpg")
    private String imageUrl;
}
