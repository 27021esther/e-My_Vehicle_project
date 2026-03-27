package com.vehicletrading.controller;

import com.vehicletrading.dto.ListingRequest;
import com.vehicletrading.dto.ListingResponse;
import com.vehicletrading.enums.ListingStatus;
import com.vehicletrading.service.ListingService;
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
@RequestMapping("/api/listings")
@RequiredArgsConstructor
@Tag(name = "Listings", description = "Vehicle listing CRUD operations")
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new listing",
        responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ListingResponse.class))))
    public ResponseEntity<ListingResponse> create(@Valid @RequestBody ListingRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(listingService.create(request, userDetails.getUsername()));
    }

    @GetMapping
    @Operation(summary = "Get all listings (public)")
    public ResponseEntity<List<ListingResponse>> getAll() {
        return ResponseEntity.ok(listingService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get listing by ID (public)")
    public ResponseEntity<ListingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.getById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get listings by status")
    public ResponseEntity<List<ListingResponse>> getByStatus(@PathVariable ListingStatus status) {
        return ResponseEntity.ok(listingService.getByStatus(status));
    }

    @GetMapping("/seller/{sellerId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get listings by seller")
    public ResponseEntity<List<ListingResponse>> getBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(listingService.getBySeller(sellerId));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update listing")
    public ResponseEntity<ListingResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ListingRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(listingService.update(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete listing (ADMIN only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        listingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
