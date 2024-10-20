package com.carrentalproj.repository;

import com.carrentalproj.entity.Reservation;

import java.sql.SQLException;
import java.util.List;

public interface ReservationRepository {

    Reservation findById(int id);
    List<Reservation> findAll();
    int save(Reservation reservation) throws SQLException;
    void delete(int id);
}
