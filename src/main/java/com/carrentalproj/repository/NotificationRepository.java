package com.carrentalproj.repository;

import com.carrentalproj.entity.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationRepository {

    Notification findById(int id);
    List<Notification> findAll();
    int save(Notification notification) throws SQLException;
    void delete(int id);
}
