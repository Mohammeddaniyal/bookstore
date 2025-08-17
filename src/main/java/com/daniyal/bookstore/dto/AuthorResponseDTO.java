package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO representing an author")
public class AuthorResponseDTO {
    @Schema(description = "Unique identifier of the author", example = "1")
    private Long id;
    @Schema(description = "Author's full name", example = "Robert C. Martin")
    private String name;
}
