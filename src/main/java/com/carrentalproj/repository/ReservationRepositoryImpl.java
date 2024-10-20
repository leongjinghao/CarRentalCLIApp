package com.carrentalproj.repository;

import com.carrentalproj.entity.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final List<Reservation> reservations;
    private int reservationId;

    private static final Lock lock = new ReentrantLock();

    private static ReservationRepository instance;

    private ReservationRepositoryImpl() {
        reservations = new ArrayList<>();
        reservationId = 1;
    }

    public static ReservationRepository getInstance() {
        lock.lock();

        if (instance == null) {
            instance = new ReservationRepositoryImpl();
        }

        lock.unlock();

        return instance;
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
        lock.lock();

        reservation.setId(reservationId++);
        reservations.add(reservation);

        lock.unlock();
    }

    @Override
    public void delete(int id) {
        lock.lock();

        reservations.stream()
                .filter(reservation -> reservation.getId() == id).findFirst()
                .ifPresent(reservation -> reservations.remove(reservation));

        lock.unlock();
    }
}
