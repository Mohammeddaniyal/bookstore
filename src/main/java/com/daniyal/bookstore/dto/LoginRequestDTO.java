package com.daniyal.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @Email(message="Email should be valid")
    @NotBlank(message="Email is required")
    private String email;
    @NotBlank(message="Password is required")
    private String password;
}
