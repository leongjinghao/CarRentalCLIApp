package com.carrentalproj.repository;

import com.carrentalproj.entity.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RentalRepositoryImpl implements RentalRepository {

    private final List<Rental> rentals;
    private int rentalId;

    private final Lock lock;

    public RentalRepositoryImpl() {
        rentals = new ArrayList<>();
        rentalId = 1;

        lock = new ReentrantLock();
    }

    @Override
    public Rental findById(int id) {
        return rentals.stream()
                .filter(rental -> rental.getId() == id). findFirst().get();
    }

    @Override
    public List<Rental> findAll() {
        return rentals;
    }

    @Override
    public void save(Rental rental) {
        lock.lock();

        rental.setId(rentalId++);
        rentals.add(rental);

        lock.unlock();
    }

    @Override
    public void delete(int id) {
        lock.lock();

        rentals.stream()
                .filter(rental -> rental.getId() == id).findFirst()
                .ifPresent(rental -> rentals.remove(rental));

        lock.unlock();
    }
}
