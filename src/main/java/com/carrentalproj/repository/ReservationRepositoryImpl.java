package com.carrentalproj.repository;

import com.carrentalproj.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final List<Reservation> reservations;
    private int reservationId;

    public ReservationRepositoryImpl() {
        reservations = new ArrayList<>();
        reservationId = 1;
    }

    @Override
    public Reservation findById(int id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id).findFirst().get();
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public void save(Reservation reservation) {
        reservation.setId(reservationId++);
        reservations.add(reservation);
    }

    @Override
    public void delete(int id) {
        reservations.stream()
                .filter(reservation -> reservation.getId() == id).findFirst()
                .ifPresent(reservation -> reservations.remove(reservation));
    }
}
