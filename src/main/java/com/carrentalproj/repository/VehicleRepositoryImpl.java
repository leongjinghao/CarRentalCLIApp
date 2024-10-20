package com.carrentalproj.repository;

import com.carrentalproj.entity.vehicleType.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VehicleRepositoryImpl implements VehicleRepository {

    private final List<Vehicle> vehicles;
    private int vehicleId;

    private final Lock lock;

    public VehicleRepositoryImpl() {
        vehicles = new ArrayList<>();
        vehicleId = 1;

        lock = new ReentrantLock();
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
        lock.lock();

        vehicle.setId(vehicleId++);
        vehicles.add(vehicle);

        lock.unlock();
    }

    @Override
    public void delete(int id) {
        lock.lock();

        vehicles.stream()
                .filter(vehicle -> vehicle.getId() == id).findFirst()
                .ifPresent(vehicle -> vehicles.remove(vehicle));

        lock.unlock();
    }
}
