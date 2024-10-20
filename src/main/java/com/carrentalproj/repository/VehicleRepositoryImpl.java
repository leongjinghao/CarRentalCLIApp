package com.carrentalproj.repository;

import com.carrentalproj.entity.vehicleType.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VehicleRepositoryImpl implements VehicleRepository {

    private final List<Vehicle> vehicles;
    private int vehicleId;

    private static final Lock lock = new ReentrantLock();

    private static VehicleRepository instance;

    private VehicleRepositoryImpl() {
        vehicles = new ArrayList<>();
        vehicleId = 1;
    }

    public static VehicleRepository getInstance() {
        lock.lock();

        if (instance == null) {
            instance = new VehicleRepositoryImpl();
        }

        lock.unlock();

        return instance;
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
