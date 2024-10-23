package com.carrentalproj.service;

import com.carrentalproj.Utility;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Rental;
import com.carrentalproj.entity.Reservation;
import com.carrentalproj.exception.IllegalCarRentalOperationArgumentException;

import java.util.Date;
import java.util.List;

public class RentalReservationOrchestrator implements RentalService, ReservationService {

    private final RentalService rentalService;
    private final ReservationService reservationService;
    private final InventoryService inventoryService;
    private static RentalReservationOrchestrator instance;

    private RentalReservationOrchestrator() {
        rentalService = RentalServiceImp.getInstance();
        reservationService = ReservationServiceImpl.getInstance();
        inventoryService = InventoryServiceImpl.getInstance();
    }

    public static synchronized RentalReservationOrchestrator getInstance() {
        if (instance == null) {
            instance = new RentalReservationOrchestrator();
        }

        return instance;
    }

    @Override
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @Override
    public List<Rental> getRentalsByInventoryInstanceId(int inventoryInstanceId) {
        return rentalService.getRentalsByInventoryInstanceId(inventoryInstanceId);
    }

    @Override
    public List<Rental> getRentalsByMemberId(int memberId) {
        return rentalService.getRentalsByMemberId(memberId);
    }

    @Override
    public Rental getRentalsById(int id) {
        return rentalService.getRentalsById(id);
    }

    public int rent(Inventory inventoryInstance, Member member, Date dueDate) {

        // Validation on the availability of inventory instance
        Date today = Utility.getTodayDate();
        String status = inventoryInstance.getStatus();
        if (status.equals("rented")) {
            Rental closestRentalRecord = findClosestRentalRecord(inventoryInstance.getId());
            throw new IllegalCarRentalOperationArgumentException("Vehicle not available: vehicle is rented out until " + closestRentalRecord.getDueDate());
        } else if (status.equals("reserved")) {
            List<Reservation> closestReservationRecords = findPresentReservationsRecord(inventoryInstance.getId());

            for (Reservation reservation : closestReservationRecords) {
                Date existingStartDate = reservation.getStartDate();
                Date existingEndDate = reservation.getEndDate();

                if (today.before(existingEndDate) && existingStartDate.before(dueDate)) {
                    throw new IllegalCarRentalOperationArgumentException("Vehicle not available: vehicle is reserved from " + existingStartDate + " to " + existingEndDate);
                }
            }
        }

        // Set inventory instance as rented, rented status preceded all other statuses
        inventoryService.updateInventoryStatus(inventoryInstance.getId(), "rented");

        return rentalService.rent(inventoryInstance, member, dueDate);
    }

    @Override
    public void returned(int memberId, int rentalId) {
        Inventory inventoryInstance = getRentalsById(rentalId).getInventoryInstance();
        List<Reservation> closestReservationRecords = findPresentReservationsRecord(inventoryInstance.getId());

        // if there are pending reservation made on the inventory instance, retain the "reserved" status on returned
        // else marked it as "available"
        if (!closestReservationRecords.isEmpty()) {
            inventoryService.updateInventoryStatus(inventoryInstance.getId(), "reserved");
        } else {
            inventoryService.updateInventoryStatus(inventoryInstance.getId(), "available");
        }


        rentalService.returned(memberId, rentalId);
    }

    @Override
    public void chargeLatePendingReturn() {
        rentalService.chargeLatePendingReturn();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @Override
    public List<Reservation> getReservationsByInventoryInstanceId(int inventoryInstanceId) {
        return reservationService.getReservationsByInventoryInstanceId(inventoryInstanceId);
    }

    @Override
    public List<Reservation> getReservationsByMemberId(int memberId) {
        return reservationService.getReservationsByMemberId(memberId);
    }

    @Override
    public Reservation getReservationById(int id) {
        return reservationService.getReservationById(id);
    }

    public int reserve(Inventory inventoryInstance, Member member, Date startDate, Date endDate) {

        /*
         * Validation on the availability of inventory instance
         *  - If status is "rented", validate input period with current existing rental period
         *  - If status is "rented" or "reserved", validate input period with potential existing reservation(s) period
         */
        String status = inventoryInstance.getStatus();
        if (status.equals("rented")) {
            // validate input period with existing rented period
            Rental closestRentalRecord = findClosestRentalRecord(inventoryInstance.getId());
            Date dueDate = closestRentalRecord.getDueDate();

            if (startDate.compareTo(dueDate) <= 0) {
                throw new IllegalCarRentalOperationArgumentException("Vehicle not available: vehicle is rented until " + dueDate);
            }
        }
        if (!status.equals("available")) {
            List<Reservation> closestReservationRecords = findPresentReservationsRecord(inventoryInstance.getId());

            for (Reservation reservation : closestReservationRecords) {
                Date existingStartDate = reservation.getStartDate();
                Date existingEndDate = reservation.getEndDate();

                if (startDate.before(existingEndDate) && existingStartDate.before(endDate)) {
                    throw new IllegalCarRentalOperationArgumentException("Vehicle not available: vehicle is reserved during the period " + existingStartDate + " to " + existingEndDate);
                }
            }
        }

        /*
         * Dynamically set the status of inventory instance, depending on the status's precedence
         *  - Do not modify if current status is "rented", as it precedes other statuses
         *  - If current status is "reserved", no changes required
         *  - If current status is "available", update to "reserved"
         */
        if (inventoryInstance.getStatus().equals("available")) {
            inventoryService.updateInventoryStatus(inventoryInstance.getId(), "reserved");
        }

        return reservationService.reserve(inventoryInstance, member, startDate, endDate);
    }

    @Override
    public void cancelReservation(int memberId, int reservationId) {
        reservationService.cancelReservation(memberId, reservationId);
    }

    public List<Reservation> findPresentReservationsRecord(int inventoryInstanceId) {
        Date today = Utility.getTodayDate();

        List<Reservation> existingReservations = reservationService.getReservationsByInventoryInstanceId(inventoryInstanceId);

        return existingReservations.stream()
                .filter(reservation -> !reservation.getEndDate().before(today)).toList();
    }

    public Rental findClosestRentalRecord(int inventoryInstanceId) {
        Date today = Utility.getTodayDate();

        List<Rental> existingRentals = rentalService.getRentalsByInventoryInstanceId(inventoryInstanceId);

        return existingRentals.stream()
                .filter(rental -> !rental.getDueDate().before(today))
                .min((r1, r2) -> r1.getStartDate().compareTo(r2.getStartDate())).get();
    }
}
