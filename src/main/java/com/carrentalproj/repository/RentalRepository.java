package com.carrentalproj.repository;

import com.carrentalproj.entity.Rental;

import java.util.List;

public interface RentalRepository {

    Rental findById(int id);
    List<Rental> findAll();
    void save(Rental rental);
    void delete(int id);
}
