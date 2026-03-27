package com.vehicletrading.dto;

import com.vehicletrading.enums.ListingStatus;
import com.vehicletrading.model.Listing;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ListingResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal askingPrice;
    private ListingStatus status;
    private LocalDateTime createdAt;
    private String sellerUsername;
    private Long vehicleId;
    private String vehicleSummary;

    public static ListingResponse from(Listing l) {
        ListingResponse r = new ListingResponse();
        r.setId(l.getId());
        r.setTitle(l.getTitle());
        r.setDescription(l.getDescription());
        r.setAskingPrice(l.getAskingPrice());
        r.setStatus(l.getStatus());
        r.setCreatedAt(l.getCreatedAt());
        r.setSellerUsername(l.getSeller().getUsername());
        r.setVehicleId(l.getVehicle().getId());
        r.setVehicleSummary(l.getVehicle().getYear() + " " + l.getVehicle().getMake() + " " + l.getVehicle().getModel());
        return r;
    }
}
