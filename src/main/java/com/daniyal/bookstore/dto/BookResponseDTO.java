package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@Builder
@Schema(description = "Response DTO representing book details")
public class BookResponseDTO {

    @Schema(description = "Unique identifier of the book", example = "8")
    private Long id;

    @Schema(description = "Title of the book", example = "Clean Code")
    private String title;

    @Schema(description = "Set of author names", example = "[\"Robert C. Martin\", \"Martin Fowler\"]")
    private Set<String> authors;

    @Schema(description = "Genre or category of the book", example = "Programming")
    private String genre;

    @Schema(description = "ISBN number of the book", example = "978-0132350884")
    private String isbn;

    @Schema(description = "Brief description of the book", example = "Updated handbook of agile software craftsmanship")
    private String description;

    @Schema(description = "Price of the book", example = "39.99")
    private BigDecimal price;

    @Schema(description = "Available quantity in stock", example = "7")
    private int quantity;

    @Schema(description = "URL to book's cover image", example = "http://example.com/cleancode.jpg")
    private String imageUrl;
}
