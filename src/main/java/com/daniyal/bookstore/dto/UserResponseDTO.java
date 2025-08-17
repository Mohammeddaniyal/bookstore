package com.daniyal.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "Response DTO for user details")
public class UserResponseDTO {
    @Schema(description = "Unique identifier of the user", example = "15")
    private Long id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Email address of the user", example = "john@example.com")
    private String email;

    @Schema(description = "Set of roles assigned to the user", example = "[\"CUSTOMER\", \"ADMIN\"]")
    private Set<String> roles;
}
