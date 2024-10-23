package com.carrentalproj.service;

import com.carrentalproj.Utility;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Notification;
import com.carrentalproj.entity.Rental;
import com.carrentalproj.exception.IllegalCarRentalOperationArgumentException;
import com.carrentalproj.repository.NotificationRepository;
import com.carrentalproj.repository.NotificationRepositoryImpl;
import com.carrentalproj.repository.RentalRepository;
import com.carrentalproj.repository.RentalRepositoryImpl;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class RentalServiceImp implements RentalService {

    private final RentalRepository rentalRepository;
    private final NotificationRepository notificationRepository;
    private static RentalServiceImp instance;

    private RentalServiceImp() {
        rentalRepository = RentalRepositoryImpl.getInstance();
        notificationRepository = NotificationRepositoryImpl.getInstance();
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
            throw new IllegalCarRentalOperationArgumentException("Due date cannot be before today's date");
        }

        long period = Duration.between(today.toInstant(), dueDate.toInstant()).toDays() + 1L;

        Rental rental = new Rental(
                0,
                inventoryInstance,
                member,
                dueDate,
                period * inventoryInstance.getRateOfRental()
        );

        try {
            return rentalRepository.save(rental);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returned(int memberId, int rentalId) {
        try {
            Rental rental = rentalRepository.findById(rentalId);

            if (rental.getMember().getId() == memberId) {
                rental.setReturned(true);
                rentalRepository.save(rental);
            } else {
                throw new IllegalCarRentalOperationArgumentException("Rental return failed, member is not the owner of the rental");
            }
        } catch (NoSuchElementException e) {
            throw new IllegalCarRentalOperationArgumentException(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    }
}
