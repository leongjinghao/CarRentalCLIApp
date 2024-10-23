package com.carrentalproj.service;

import com.carrentalproj.entity.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getAllNotifications();
    Notification getNotification(int id);
    int addNotification(Notification notification);
    void updateNotification(int id, String message);
    void removeNotification(int id);

}
