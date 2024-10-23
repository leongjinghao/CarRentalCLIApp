package com.carrentalproj.service;

import com.carrentalproj.Utility;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Reservation;
import com.carrentalproj.exception.IllegalCarRentalOperationArgumentException;
import com.carrentalproj.repository.ReservationRepository;
import com.carrentalproj.repository.ReservationRepositoryImpl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private static ReservationServiceImpl instance;

    private ReservationServiceImpl() {
        reservationRepository = ReservationRepositoryImpl.getInstance();
    }

    public static synchronized ReservationServiceImpl getInstance() {
        if (instance == null) {
            instance = new ReservationServiceImpl();
        }

        return instance;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByInventoryInstanceId(int inventoryInstanceId) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getInventoryInstance().getId() == inventoryInstanceId).toList();
    }

    @Override
    public List<Reservation> getReservationsByMemberId(int memberId) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getMember().getId() == memberId).toList();
    }

    @Override
    public Reservation getReservationById(int id) {
        return reservationRepository.findById(id);
    }

    @Override
    public int reserve(Inventory inventoryInstance, Member member, Date startDate, Date endDate) {

        // Validation on startDate and endDate
        Date today = Utility.getTodayDate();
        if (startDate.before(today)) {
            throw new IllegalCarRentalOperationArgumentException("Start date cannot be before today's date");
        }
        if (startDate.after(endDate)) {
            throw new IllegalCarRentalOperationArgumentException("Start date cannot be after end date");
        }

        Reservation reservation = new Reservation(
                0,
                inventoryInstance,
                member,
                startDate,
                endDate
        );

        try {
            return reservationRepository.save(reservation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelReservation(int memberId, int reservationId) {
        try {
            Reservation reservation = reservationRepository.findById(reservationId);

            if (reservation.getMember().getId() == memberId) {
                reservationRepository.delete(reservationId);
            } else {
                throw new IllegalCarRentalOperationArgumentException("Reservation cancellation failed, member is not the owner of the reservation");
            }

        } catch (NoSuchElementException e) {
            throw new IllegalCarRentalOperationArgumentException(e.getMessage());
        }
    }
}
