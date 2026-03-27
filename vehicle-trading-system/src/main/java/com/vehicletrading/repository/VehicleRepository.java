package com.vehicletrading.repository;

import com.vehicletrading.enums.VehicleStatus;
import com.vehicletrading.enums.VehicleType;
import com.vehicletrading.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByOwnerId(Long ownerId);
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByType(VehicleType type);
    List<Vehicle> findByMakeIgnoreCaseAndModelIgnoreCase(String make, String model);
}
