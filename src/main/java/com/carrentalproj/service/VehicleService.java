package com.carrentalproj.service;

import com.carrentalproj.entity.vehicleType.Vehicle;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAllVehicleTypes();
    Vehicle getVehicleType(int id);
    int addVehicleType(Vehicle vehicle);
    void removeVehicleType(int id);
}
