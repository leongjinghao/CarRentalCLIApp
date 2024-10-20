package com.carrentalproj.service;

import com.carrentalproj.Utility;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Rental;
import com.carrentalproj.repository.RentalRepository;
import com.carrentalproj.repository.RentalRepositoryImpl;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RentalServiceImp implements RentalService {

    private final RentalRepository rentalRepository;
    private static RentalServiceImp instance;

    private RentalServiceImp() {
        rentalRepository = RentalRepositoryImpl.getInstance();
    }

    public static synchronized RentalServiceImp getInstance() {
        if (instance == null) {
            instance = new RentalServiceImp();
        }

        return instance;
    }

    @Override
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    public List<Rental> getRentalsByInventoryInstanceId(int inventoryInstanceId) {
        return rentalRepository.findAll().stream()
                .filter(rental -> rental.getInventoryInstance().getId() == inventoryInstanceId).toList();
    }

    @Override
    public List<Rental> getRentalsByMemberId(int memberId) {
        return rentalRepository.findAll().stream()
                .filter(rental -> rental.getMember().getId() == memberId).toList();
    }

    @Override
    public Rental getRentalsById(int id) {
        return rentalRepository.findById(id);
    }

    @Override
    public int rent(Inventory inventoryInstance, Member member, Date dueDate) {

        // Validation on dueDate
        Date today = Utility.getTodayDate();
        if (dueDate.before(today)) {
            throw new IllegalArgumentException("Due date cannot be before today's date");
        }

        long period = Duration.between(today.toInstant(), dueDate.toInstant()).toDays() + 1L;

        Rental rental = new Rental(
                0,
                inventoryInstance,
                member,
                dueDate,
                period * inventoryInstance.getRateOfRental()
        );

        rentalRepository.save(rental);
        return rental.getId();
    }

    @Override
    public void returned(int id) {
        Rental rentalToRelease = rentalRepository.findById(id);
        rentalToRelease.setReturned(true);
    }
}
