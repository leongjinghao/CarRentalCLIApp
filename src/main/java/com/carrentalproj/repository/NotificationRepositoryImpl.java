package com.carrentalproj.repository;

import com.carrentalproj.databaseconnection.DatabaseConnection;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NotificationRepositoryImpl implements NotificationRepository {

    private final DatabaseConnection databaseConnection;

    private static final Lock lock = new ReentrantLock();

    private static NotificationRepository instance;

    private NotificationRepositoryImpl() {
        databaseConnection = DatabaseConnection.getInstance();
    }

    public static NotificationRepository getInstance() {
        try {
            lock.lock();

            if (instance == null) {
                instance = new NotificationRepositoryImpl();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    @Override
    public Notification findById(int id) {
        Notification notification = null;

        try (Connection connection = databaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM notification WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                notification = new Notification(
                        resultSet.getInt(1),
                        MemberRepositoryImpl.getInstance().findById(
                                resultSet.getInt(2)),
                        resultSet.getString(3)
                );
            } else {
                throw new NoSuchElementException("No notification with ID=" + id + " found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notification;
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM notification");

            while (resultSet.next()) {
                Notification notification = new Notification(
                        resultSet.getInt(1),
                        MemberRepositoryImpl.getInstance().findById(
                                resultSet.getInt(2)),
                        resultSet.getString(3)
                );

                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    @Override
    public int save(Notification notification) throws SQLException {
        int idOnCreate = 0;

        try {
            lock.lock();

            try (Connection connection = databaseConnection.getConnection()) {
                PreparedStatement preparedStatement;

                // Update
                try {
                    findById(notification.getId());

                    preparedStatement = connection.prepareStatement("""
                            UPDATE notification
                            SET memberId = ?, message = ?
                            WHERE id = %d
                            """.formatted(notification.getId()));
                }
                // Create
                catch (NoSuchElementException e) {
                    preparedStatement = connection.prepareStatement("""
                            INSERT INTO notification(memberId, message)
                            VALUES
                            (?, ?)
                            """);
                }

                if (preparedStatement != null) {
                    preparedStatement.setInt(1, notification.getMember().getId());
                    preparedStatement.setString(2, notification.getMessage());
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
                        "DELETE FROM notification WHERE id = ?");
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
