package com.vehicletrading.dto;

import com.vehicletrading.enums.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request body for creating or updating a vehicle")
public class VehicleRequest {
    @Schema(description = "Vehicle manufacturer", example = "Toyota") @NotBlank  private String make;
    @Schema(description = "Vehicle model", example = "Corolla") @NotBlank  private String model;
    @Schema(description = "Manufacturing year", example = "2020") @NotNull @Min(1900) private Integer year;
    @Schema(description = "Vehicle color", example = "Red") private String color;
    @Schema(description = "Mileage in km", example = "45000") @Min(0) private Integer mileage;
    @Schema(description = "Price in USD", example = "15000.00") @DecimalMin("0.0") private BigDecimal price;
    @Schema(description = "Vehicle type", example = "CAR") private VehicleType type;
}
