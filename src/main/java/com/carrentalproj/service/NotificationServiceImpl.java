package com.carrentalproj.service;

import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Notification;
import com.carrentalproj.repository.NotificationRepository;
import com.carrentalproj.repository.NotificationRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private static NotificationServiceImpl instance;

    private NotificationServiceImpl() {
        notificationRepository = NotificationRepositoryImpl.getInstance();
    }

    public static synchronized NotificationServiceImpl getInstance() {
        if (instance == null) {
            instance = new NotificationServiceImpl();
        }

        return instance;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotification(int id) {
        return notificationRepository.findById(id);
    }

    @Override
    public int addNotification(Notification notification) {
        try {
            return notificationRepository.save(notification);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateNotification(int id, String message) {
        try {
            Notification notification = notificationRepository.findById(id);
            notification.setMessage(message);
            notificationRepository.save(notification);
        } catch (java.sql.SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public void removeNotification(int id) {
        notificationRepository.delete(id);
    }
}
