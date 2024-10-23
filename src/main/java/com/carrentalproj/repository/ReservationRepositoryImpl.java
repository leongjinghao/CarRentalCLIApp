package com.carrentalproj.repository;

import com.carrentalproj.databaseconnection.DatabaseConnection;
import com.carrentalproj.entity.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final DatabaseConnection databaseConnection;

    private static final Lock lock = new ReentrantLock();

    private static ReservationRepository instance;

    private ReservationRepositoryImpl() {
        databaseConnection = DatabaseConnection.getInstance();
    }

    public static ReservationRepository getInstance() {
        try {
            lock.lock();

            if (instance == null) {
                instance = new ReservationRepositoryImpl();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    @Override
    public Reservation findById(int id) {
        Reservation reservation = null;

        try (Connection connection = databaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM reservation WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reservation = new Reservation(
                        resultSet.getInt(1),
                        InventoryRepositoryImpl.getInstance().findById(
                                resultSet.getInt(2)),
                        MemberRepositoryImpl.getInstance().findById(
                                resultSet.getInt(3)),
                        resultSet.getDate(4),
                        resultSet.getDate(5)
                );
            } else {
                throw new NoSuchElementException("No reservation record with ID=" + id + " found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation");

            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt(1),
                        InventoryRepositoryImpl.getInstance().findById(
                                resultSet.getInt(2)),
                        MemberRepositoryImpl.getInstance().findById(
                                resultSet.getInt(3)),
                        resultSet.getDate(4),
                        resultSet.getDate(5)
                );

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public int save(Reservation reservation) throws SQLException {
        int idOnCreate = 0;

        try {
            lock.lock();

            try (Connection connection = databaseConnection.getConnection()) {
                PreparedStatement preparedStatement;

                // Update
                try {
                    findById(reservation.getId());

                    preparedStatement = connection.prepareStatement("""
                            UPDATE reservation
                            SET inventoryId = ?, memberId = ?, startDate = ?, endDate = ?
                            WHERE id = %d
                            """.formatted(reservation.getId()));
                }
                // Create
                catch (NoSuchElementException e) {
                    preparedStatement = connection.prepareStatement("""
                            INSERT INTO reservation(inventoryId, memberId, startDate, endDate)
                            VALUES
                            (?, ?, ?, ?)
                            """);
                }

                if (preparedStatement != null) {
                    preparedStatement.setInt(1, reservation.getInventoryInstance().getId());
                    preparedStatement.setInt(2, reservation.getMember().getId());
                    preparedStatement.setDate(3, new Date(reservation.getStartDate().getTime()));
                    preparedStatement.setDate(4, new Date(reservation.getEndDate().getTime()));
                    preparedStatement.executeUpdate();

                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
                    resultSet.next();
                    idOnCreate = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }

        return idOnCreate;
    }

    @Override
    public void delete(int id) {
        try {
            lock.lock();

            try (Connection connection = databaseConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM reservation WHERE id = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }
}
