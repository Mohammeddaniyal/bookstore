package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Login request containing email and password")
public class LoginRequestDTO {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "User's password", example = "password123", required = true)
    private String password;
}
