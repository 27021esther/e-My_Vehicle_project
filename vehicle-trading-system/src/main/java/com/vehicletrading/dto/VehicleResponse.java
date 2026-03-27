package com.vehicletrading.dto;

import com.vehicletrading.enums.VehicleStatus;
import com.vehicletrading.enums.VehicleType;
import com.vehicletrading.model.Vehicle;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleResponse {
    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private Integer mileage;
    private BigDecimal price;
    private VehicleType type;
    private VehicleStatus status;
    private String ownerUsername;

    public static VehicleResponse from(Vehicle v) {
        VehicleResponse r = new VehicleResponse();
        r.setId(v.getId());
        r.setMake(v.getMake());
        r.setModel(v.getModel());
        r.setYear(v.getYear());
        r.setColor(v.getColor());
        r.setMileage(v.getMileage());
        r.setPrice(v.getPrice());
        r.setType(v.getType());
        r.setStatus(v.getStatus());
        r.setOwnerUsername(v.getOwner().getUsername());
        return r;
    }
}
