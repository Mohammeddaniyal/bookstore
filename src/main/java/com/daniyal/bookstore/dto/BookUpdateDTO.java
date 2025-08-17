package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO for updating book details. All fields are optional.")
public class BookUpdateDTO {

    @Schema(description = "Updated title of the book", example = "Clean Code Refactored")
    private String title;

    @Schema(description = "Updated set of author IDs", example = "[1, 3]")
    private Set<Long> authorIds;

    @Schema(description = "Updated genre", example = "Software Engineering")
    private String genre;

    @Schema(description = "Updated brief description", example = "New description about code quality")
    private String description;

    @Positive(message = "The price must be positive")
    @Schema(description = "Updated price of the book", example = "42.99")
    private BigDecimal price;

    @PositiveOrZero(message = "The quantity cannot be negative")
    @Schema(description = "Updated quantity in stock", example = "12")
    private Integer quantity;

    @Schema(description = "Updated URL to book's cover image", example = "http://example.com/newimage.jpg")
    private String imageUrl;
}
