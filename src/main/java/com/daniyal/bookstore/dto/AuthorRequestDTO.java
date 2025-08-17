package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating or updating an author")
public class AuthorRequestDTO {

    @NotBlank(message="Name is required")
    @Schema(description = "Author's full name", example = "Robert C. Martin", required = true)
    private String name;
}
