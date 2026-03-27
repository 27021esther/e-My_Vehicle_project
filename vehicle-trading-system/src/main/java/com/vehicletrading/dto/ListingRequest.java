package com.vehicletrading.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request body for creating or updating a listing")
public class ListingRequest {
    @Schema(description = "Listing title", example = "2020 Toyota Corolla for sale") @NotBlank  private String title;
    @Schema(description = "Listing description", example = "Well maintained, single owner") private String description;
    @Schema(description = "Asking price in USD", example = "14500.00") @NotNull @DecimalMin("0.0") private BigDecimal askingPrice;
    @Schema(description = "ID of the vehicle being listed", example = "1") @NotNull   private Long vehicleId;
}
