package com.vehicletrading.service;

import com.vehicletrading.dto.ListingRequest;
import com.vehicletrading.dto.ListingResponse;
import com.vehicletrading.enums.ListingStatus;
import com.vehicletrading.model.Listing;
import com.vehicletrading.model.User;
import com.vehicletrading.model.Vehicle;
import com.vehicletrading.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final VehicleService vehicleService;
    private final UserService userService;

    public ListingResponse create(ListingRequest request, String username) {
        User seller = (User) userService.loadUserByUsername(username);
        Vehicle vehicle = vehicleService.findById(request.getVehicleId());

        Listing listing = Listing.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .askingPrice(request.getAskingPrice())
                .status(ListingStatus.ACTIVE)
                .vehicle(vehicle)
                .seller(seller)
                .build();
        return ListingResponse.from(listingRepository.save(listing));
    }

    public List<ListingResponse> getAll() {
        return listingRepository.findAll().stream().map(ListingResponse::from).collect(Collectors.toList());
    }

    public ListingResponse getById(Long id) {
        return ListingResponse.from(findById(id));
    }

    public List<ListingResponse> getBySeller(Long sellerId) {
        return listingRepository.findBySellerId(sellerId).stream().map(ListingResponse::from).collect(Collectors.toList());
    }

    public List<ListingResponse> getByStatus(ListingStatus status) {
        return listingRepository.findByStatus(status).stream().map(ListingResponse::from).collect(Collectors.toList());
    }

    public ListingResponse update(Long id, ListingRequest request, String username) {
        Listing listing = findById(id);
        if (!listing.getSeller().getUsername().equals(username))
            throw new RuntimeException("Not authorized to update this listing");

        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setAskingPrice(request.getAskingPrice());
        return ListingResponse.from(listingRepository.save(listing));
    }

    public void delete(Long id) {
        listingRepository.deleteById(id);
    }

    private Listing findById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found: " + id));
    }
}
