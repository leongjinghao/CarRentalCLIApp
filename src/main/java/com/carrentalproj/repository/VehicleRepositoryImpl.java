package com.carrentalproj.repository;

import com.carrentalproj.entity.vehicleType.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRepositoryImpl implements VehicleRepository {

    private final List<Vehicle> vehicles;
    private int vehicleId;

    public VehicleRepositoryImpl() {
        vehicles = new ArrayList<>();
        vehicleId = 1;
    }

    @Override
    public Vehicle findById(int id) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getId() == id).findFirst().get();
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicles;
    }

    @Override
    public void save(Vehicle vehicle) {
        vehicle.setId(vehicleId++);
        vehicles.add(vehicle);
    }

    @Override
    public void delete(int id) {
        vehicles.stream()
                .filter(vehicle -> vehicle.getId() == id).findFirst()
                .ifPresent(vehicle -> vehicles.remove(vehicle));
    }
}
