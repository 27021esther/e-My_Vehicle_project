package com.vehicletrading.controller;

import com.vehicletrading.dto.VehicleRequest;
import com.vehicletrading.dto.VehicleResponse;
import com.vehicletrading.enums.VehicleStatus;
import com.vehicletrading.service.VehicleService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Vehicle CRUD operations")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a new vehicle",
        responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = VehicleResponse.class))))
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(vehicleService.create(request, userDetails.getUsername()));
    }

    @GetMapping
    @Operation(summary = "Get all vehicles (public)")
    public ResponseEntity<List<VehicleResponse>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID (public)")
    public ResponseEntity<VehicleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get vehicles by status")
    public ResponseEntity<List<VehicleResponse>> getByStatus(@PathVariable VehicleStatus status) {
        return ResponseEntity.ok(vehicleService.getByStatus(status));
    }

    @GetMapping("/owner/{ownerId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get vehicles by owner")
    public ResponseEntity<List<VehicleResponse>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(vehicleService.getByOwner(ownerId));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update vehicle")
    public ResponseEntity<VehicleResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody VehicleRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(vehicleService.update(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete vehicle (ADMIN only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
