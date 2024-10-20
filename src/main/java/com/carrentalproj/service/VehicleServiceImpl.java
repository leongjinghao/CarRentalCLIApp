package com.carrentalproj.service;

import com.carrentalproj.entity.vehicleType.Vehicle;
import com.carrentalproj.repository.VehicleRepository;
import com.carrentalproj.repository.VehicleRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private static VehicleServiceImpl instance;

    private VehicleServiceImpl() {
        vehicleRepository = VehicleRepositoryImpl.getInstance();
    }

    public static synchronized VehicleServiceImpl getInstance() {
        if (instance == null) {
            instance = new VehicleServiceImpl();
        }

        return instance;
    }

    @Override
    public List<Vehicle> getAllVehicleTypes() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleType(int id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public int addVehicleType(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        return vehicle.getId();
    }

    @Override
    public void removeVehicleType(int id) {
        vehicleRepository.delete(id);
    }
}
