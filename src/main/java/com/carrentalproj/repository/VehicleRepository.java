package com.carrentalproj.repository;

import com.carrentalproj.entity.vehicleType.Vehicle;

import java.sql.SQLException;
import java.util.List;

public interface VehicleRepository {

    Vehicle findById(int id);
    List<Vehicle> findAll();
    int save(Vehicle vehicle) throws SQLException;
    void delete(int id);
}
