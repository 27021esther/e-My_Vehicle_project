package com.vehicletrading.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request body for user registration")
public class RegisterRequest {
    @Schema(description = "Unique username", example = "john_doe") @NotBlank @Size(min = 3, max = 50) private String username;
    @Schema(description = "User email address", example = "john@example.com") @NotBlank @Email private String email;
    @Schema(description = "Password (min 6 characters)", example = "secret123") @NotBlank @Size(min = 6) private String password;
}
