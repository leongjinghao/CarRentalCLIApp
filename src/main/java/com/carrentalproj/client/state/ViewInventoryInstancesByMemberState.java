package com.carrentalproj.client.state;

import com.carrentalproj.Utility;
import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Rental;
import com.carrentalproj.entity.Reservation;
import com.carrentalproj.service.RentalReservationOrchestrator;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ViewInventoryInstancesByMemberState implements ClientState {

    private final ClientContext clientContext;

    public ViewInventoryInstancesByMemberState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        RentalReservationOrchestrator rentalReservationOrchestrator = RentalReservationOrchestrator.getInstance();

        System.out.println("Viewing Inventory Instance Rented or Reserved by Member...");
        System.out.println("Please enter the member's ID:");
        int memberIdSelected = sc.nextInt();

        List<Rental> rentalsByMember = rentalReservationOrchestrator.getRentalsByMemberId(memberIdSelected);
        List<Reservation> reservationsByMember = rentalReservationOrchestrator.getReservationsByMemberId(memberIdSelected);

        HashSet<Inventory> rentalsByMemberFilteredCurrent = new HashSet<>(rentalsByMember.stream()
                .filter(rental -> !rental.isReturned())
                .map(rental -> rental.getInventoryInstance())
                .collect(Collectors.toMap(
                        inventoryInstance -> inventoryInstance.getId(),
                        inventoryInstance -> inventoryInstance,
                        (existing, replacement) -> existing
                ))
                .values());

        HashSet<Inventory> reservationsByMemberFilteredCurrent = new HashSet<>(reservationsByMember.stream()
                .filter(reservation -> reservation.getEndDate().after(Utility.getTodayDate()))
                .map(reservation -> reservation.getInventoryInstance())
                .collect(Collectors.toMap(
                        inventoryInstance -> inventoryInstance.getId(),
                        inventoryInstance -> inventoryInstance,
                        (existing, replacement) -> existing
                ))
                .values());

        if (rentalsByMemberFilteredCurrent.isEmpty() && reservationsByMemberFilteredCurrent.isEmpty()) {
            System.out.println("\nNo result found...");
        } else {
            if (!rentalsByMemberFilteredCurrent.isEmpty()) {
                System.out.println("\nList of inventory instance(s) currently rented by member (ID=" + memberIdSelected + "):");
                rentalsByMemberFilteredCurrent.forEach(
                        inventoryInstance -> System.out.println(inventoryInstance));
            }

            if (!reservationsByMemberFilteredCurrent.isEmpty()) {
                System.out.println("\nList of inventory instance(s) currently reserved by member (ID=" + memberIdSelected + "):");
                reservationsByMemberFilteredCurrent.forEach(
                        inventoryInstance -> System.out.println(inventoryInstance));
            }
        }

        clientContext.setClientState(new PendingContinueState(clientContext));
    }
}
