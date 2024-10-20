package com.carrentalproj.repository;

import com.carrentalproj.databaseconnection.DatabaseConnection;
import com.carrentalproj.entity.Rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RentalRepositoryImpl implements RentalRepository {

    Connection connection;

    private static final Lock lock = new ReentrantLock();

    private static RentalRepository instance;

    private RentalRepositoryImpl() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
    }

    public static RentalRepository getInstance() {
        lock.lock();

        if (instance == null) {
            instance = new RentalRepositoryImpl();
        }

        lock.unlock();
        return instance;
    }

    @Override
    public Rental findById(int id) {
        Rental rental = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM rental WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                rental = new Rental(
                        resultSet.getInt(1),
                        InventoryRepositoryImpl.getInstance().findById(
                                resultSet.getInt(2)),
                        MemberRepositoryImpl.getInstance().findById(
                                resultSet.getInt(3)),
                        resultSet.getDate(5),
                        resultSet.getDouble(6)
                );
            } else {
                throw new NoSuchElementException("No rental record with ID=" + id + " found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rental;
    }

    @Override
    public List<Rental> findAll() {
        List<Rental> rentals = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rental");

            while (resultSet.next()) {
                Rental rental = new Rental(
                        resultSet.getInt(1),
                        InventoryRepositoryImpl.getInstance().findById(
                                resultSet.getInt(2)),
                        MemberRepositoryImpl.getInstance().findById(
                                resultSet.getInt(3)),
                        resultSet.getDate(5),
                        resultSet.getDouble(6)
                );

                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public int save(Rental rental) throws SQLException {
        lock.lock();

        int id = rental.getId();
        PreparedStatement preparedStatement;

        // Update
        try {
            findById(rental.getId());

            preparedStatement = connection.prepareStatement("""
                    UPDATE rental
                    SET inventoryId = ?, memberId = ?, startDate = ?, dueDate = ?, rentalFee = ?, lateFee = ?, isReturned = ?
                    WHERE id = %d
                    """.formatted(rental.getId()));
        }
        // Create
        catch (NoSuchElementException e) {
            preparedStatement = connection.prepareStatement("""
                    INSERT INTO rental(inventoryId, memberId, startDate, dueDate, rentalFee, lateFee, isReturned)
                    VALUES
                    (?, ?, ?, ?, ?, ?, ?)
                    """);
        }

        if (preparedStatement != null) {
            preparedStatement.setInt(1, rental.getInventoryInstance().getId());
            preparedStatement.setInt(2, rental.getMember().getId());
            preparedStatement.setDate(3, new Date(rental.getStartDate().getTime()));
            preparedStatement.setDate(4, new Date(rental.getDueDate().getTime()));
            preparedStatement.setDouble(5, rental.getRentalFee());
            preparedStatement.setDouble(6, rental.getLateFee());
            preparedStatement.setBoolean(7, rental.isReturned());
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
                    "DELETE FROM rental WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lock.unlock();
    }
}
