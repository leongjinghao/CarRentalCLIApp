package com.carrentalproj.repository;

import com.carrentalproj.databaseconnection.DatabaseConnection;
import com.carrentalproj.entity.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InventoryRepositoryImpl implements InventoryRepository {

    Connection connection;

    private static final Lock lock = new ReentrantLock();

    private static InventoryRepository instance;

    private InventoryRepositoryImpl() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
    }

    public static InventoryRepository getInstance() {
        lock.lock();

        if (instance == null) {
            instance = new InventoryRepositoryImpl();
        }

        lock.unlock();

        return instance;
    }

    @Override
    public Inventory findById(int id) {
        Inventory inventoryInstance = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM inventory WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                inventoryInstance = new Inventory(resultSet.getInt(1), VehicleRepositoryImpl.getInstance().findById(resultSet.getInt(2)), resultSet.getString(3), resultSet.getString(4), resultSet.getDouble(5), resultSet.getString(6));
            } else {
                throw new NoSuchElementException("No inventory instance with ID=" + id + " found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryInstance;
    }

    @Override
    public List<Inventory> findAll() {
        List<Inventory> inventoryList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM inventory");

            while (resultSet.next()) {
                Inventory inventoryInstance = new Inventory(resultSet.getInt(1), VehicleRepositoryImpl.getInstance().findById(resultSet.getInt(2)), resultSet.getString(3), resultSet.getString(4), resultSet.getDouble(5), resultSet.getString(6));

                inventoryList.add(inventoryInstance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryList;
    }

    @Override
    public int save(Inventory inventory) throws SQLException {
        lock.lock();

        int id = inventory.getId();
        PreparedStatement preparedStatement;

        // Update
        try {
            findById(inventory.getId());

            preparedStatement = connection.prepareStatement("""
                    UPDATE inventory
                    SET vehicleId = ?, barcode = ?, parkingStallNum = ?, rateOfRental = ?, status = ?
                    WHERE id = %d
                    """.formatted(inventory.getId()));
        }
        // Create
        catch (NoSuchElementException e) {
            preparedStatement = connection.prepareStatement("""
                    INSERT INTO inventory(vehicleId, barcode, parkingStallNum, rateOfRental, status)
                    VALUES
                    (?, ?, ?, ?, ?)
                    """);
        }

        if (preparedStatement != null) {
            preparedStatement.setInt(1, inventory.getVehicle().getId());
            preparedStatement.setString(2, inventory.getBarcode());
            preparedStatement.setString(3, inventory.getParkingStallNum());
            preparedStatement.setDouble(4, inventory.getRateOfRental());
            preparedStatement.setString(5, inventory.getStatus());
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
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM inventory WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lock.unlock();
    }
}
