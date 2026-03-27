package com.vehicletrading.service;

import com.vehicletrading.dto.VehicleRequest;
import com.vehicletrading.dto.VehicleResponse;
import com.vehicletrading.enums.VehicleStatus;
import com.vehicletrading.enums.VehicleType;
import com.vehicletrading.model.User;
import com.vehicletrading.model.Vehicle;
import com.vehicletrading.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    public VehicleResponse create(VehicleRequest request, String username) {
        User owner = (User) userService.loadUserByUsername(username);
        Vehicle vehicle = Vehicle.builder()
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .color(request.getColor())
                .mileage(request.getMileage())
                .price(request.getPrice())
                .type(request.getType() != null ? request.getType() : VehicleType.CAR)
                .status(VehicleStatus.AVAILABLE)
                .owner(owner)
                .build();
        return VehicleResponse.from(vehicleRepository.save(vehicle));
    }

    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll().stream().map(VehicleResponse::from).collect(Collectors.toList());
    }

    public VehicleResponse getById(Long id) {
        return VehicleResponse.from(findById(id));
    }

    public List<VehicleResponse> getByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId).stream().map(VehicleResponse::from).collect(Collectors.toList());
    }

    public List<VehicleResponse> getByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status).stream().map(VehicleResponse::from).collect(Collectors.toList());
    }

    public VehicleResponse update(Long id, VehicleRequest request, String username) {
        Vehicle vehicle = findById(id);
        if (!vehicle.getOwner().getUsername().equals(username))
            throw new RuntimeException("Not authorized to update this vehicle");

        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setColor(request.getColor());
        vehicle.setMileage(request.getMileage());
        vehicle.setPrice(request.getPrice());
        if (request.getType() != null) vehicle.setType(request.getType());
        return VehicleResponse.from(vehicleRepository.save(vehicle));
    }

    public void delete(Long id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }
}
