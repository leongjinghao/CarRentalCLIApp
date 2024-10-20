package com.carrentalproj.repository;

import com.carrentalproj.entity.Rental;

import java.sql.SQLException;
import java.util.List;

public interface RentalRepository {

    Rental findById(int id);
    List<Rental> findAll();
    int save(Rental rental) throws SQLException;
    void delete(int id);
}
