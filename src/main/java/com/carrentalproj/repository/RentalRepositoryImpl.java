package com.carrentalproj.repository;

import com.carrentalproj.entity.Rental;

import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {

    private final List<Rental> rentals;
    private int rentalId;

    public RentalRepositoryImpl() {
        rentals = new ArrayList<>();
        rentalId = 1;
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
        rental.setId(rentalId++);
        rentals.add(rental);
    }

    @Override
    public void delete(int id) {
        rentals.stream()
                .filter(rental -> rental.getId() == id).findFirst()
                .ifPresent(rental -> rentals.remove(rental));
    }
}
