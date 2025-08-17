package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response after successful login")
public class LoginResponseDTO {
    @Schema(description = "Success message", example = "Login successful")
    private String message;

    @Schema(description = "JWT token for authenticated sessions", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
