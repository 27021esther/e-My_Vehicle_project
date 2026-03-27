package com.vehicletrading.repository;

import com.vehicletrading.enums.ListingStatus;
import com.vehicletrading.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findBySellerId(Long sellerId);
    List<Listing> findByVehicleId(Long vehicleId);
    List<Listing> findByStatus(ListingStatus status);
}
