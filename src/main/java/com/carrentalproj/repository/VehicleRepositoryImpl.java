package com.carrentalproj.repository;

import com.carrentalproj.databaseconnection.DatabaseConnection;
import com.carrentalproj.entity.vehicleType.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VehicleRepositoryImpl implements VehicleRepository {

    Connection connection;

    private static final Lock lock = new ReentrantLock();

    private static VehicleRepository instance;

    private VehicleRepositoryImpl() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
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
        Vehicle vehicleType = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM vehicle WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                vehicleType = new Vehicle(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            } else {
                throw new NoSuchElementException("No vehicle type with ID=" + id + " found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicleType;
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM vehicle");

            while (resultSet.next()) {
                Vehicle vehicleType = new Vehicle(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );

                vehicles.add(vehicleType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    @Override
    public int save(Vehicle vehicle) throws SQLException {
        lock.lock();

        int id = vehicle.getId();
        PreparedStatement preparedStatement;

        // Update
        try {
            findById(vehicle.getId());

            preparedStatement = connection.prepareStatement("""
                    UPDATE vehicle
                    SET brand = ?, model = ?, colour = ?
                    WHERE id = %d
                    """.formatted(vehicle.getId()));
        }
        // Create
        catch (NoSuchElementException e) {
            preparedStatement = connection.prepareStatement("""
                    INSERT INTO vehicle(brand, model, colour)
                    VALUES
                    (?, ?, ?)
                    """);
        }

        if (preparedStatement != null) {
            preparedStatement.setString(1, vehicle.getBrand());
            preparedStatement.setString(2, vehicle.getModel());
            preparedStatement.setString(3, vehicle.getColour());
            preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
            resultSet.next();
            id = resultSet.getInt(1);
        }

        lock.unlock();

        return id;
    }

    @Override
    public void delete(int id) {
        lock.lock();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM vehicle WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lock.unlock();
    }
}
