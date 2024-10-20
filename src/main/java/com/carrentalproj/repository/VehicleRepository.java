package com.carrentalproj.repository;

import com.carrentalproj.entity.vehicleType.Vehicle;

import java.util.List;

public interface VehicleRepository {

    Vehicle findById(int id);
    List<Vehicle> findAll();
    void save(Vehicle vehicle);
    void delete(int id);
}
