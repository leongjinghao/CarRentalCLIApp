package com.carrentalproj.service;

import com.carrentalproj.Utility;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Reservation;
import com.carrentalproj.repository.ReservationRepository;

import java.util.Date;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getId() == id).findFirst().get();
    }

    @Override
    public int reserve(Inventory inventoryInstance, Member member, Date startDate, Date endDate) {

        // Validation on startDate and endDate
        Date today = Utility.getTodayDate();
        if (startDate.before(today)) {
            throw new IllegalArgumentException("Start date cannot be before today's date");
        }
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Reservation reservation = new Reservation(
                0,
                inventoryInstance,
                member,
                startDate,
                endDate
        );

        reservationRepository.save(reservation);
        return reservation.getId();
    }

    @Override
    public void cancelReservation(int id) {
        reservationRepository.delete(id);
    }
}
