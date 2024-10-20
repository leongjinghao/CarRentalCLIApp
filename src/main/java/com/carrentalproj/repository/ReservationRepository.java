package com.carrentalproj.repository;

import com.carrentalproj.entity.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation findById(int id);
    List<Reservation> findAll();
    void save(Reservation reservation);
    void delete(int id);
}
