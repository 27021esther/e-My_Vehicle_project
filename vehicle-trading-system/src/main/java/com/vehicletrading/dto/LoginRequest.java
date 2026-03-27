package com.vehicletrading.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request body for login")
public class LoginRequest {
    @Schema(description = "Username", example = "john_doe") @NotBlank private String username;
    @Schema(description = "Password", example = "secret123") @NotBlank private String password;
}
