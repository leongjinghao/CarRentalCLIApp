package com.carrentalproj.client.state;

import com.carrentalproj.Utility;
import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Rental;
import com.carrentalproj.entity.Reservation;
import com.carrentalproj.service.RentalReservationOrchestrator;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ViewMembersByInventoryInstance implements ClientState {

    private final ClientContext clientContext;

    public ViewMembersByInventoryInstance(ClientContext clientContext) {
        this.clientContext = clientContext;
        clientContext.setBusinessOperation("ViewMembersByInventoryInstance");
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        RentalReservationOrchestrator rentalReservationOrchestrator = RentalReservationOrchestrator.getInstance();


        if (clientContext.getVehicleTypeSelected() == null || clientContext.getInventoryInstanceSelected() == null) {
            System.out.println("Viewing Member(s) Who Rented or Reserved a Inventory Instance...");
            clientContext.setClientState(new VehicleSelectionState(clientContext));
        } else {
            int inventoryInstanceSelectedId = clientContext.getInventoryInstanceSelected().getId();
            List<Rental> rentalsWithInventoryInstance = rentalReservationOrchestrator.getRentalsByInventoryInstanceId(inventoryInstanceSelectedId);
            List<Reservation> reservationsWithInventoryInstance = rentalReservationOrchestrator.getReservationsByInventoryInstanceId(inventoryInstanceSelectedId);

            if (!rentalsWithInventoryInstance.isEmpty()) {
                System.out.println("\nList of member(s) currently rented " + clientContext.getInventoryInstanceSelected() + ":");

                new HashSet<>(rentalsWithInventoryInstance.stream()
                        .filter(rental -> !rental.isReturned())
                        .map(rental -> rental.getMember())
                        .collect(Collectors.toMap(
                                member -> member.getId(),
                                member -> member,
                                (existing, replacement) -> existing
                        ))
                        .values())
                        .forEach(member -> System.out.println(member));
            }

            if (!reservationsWithInventoryInstance.isEmpty()) {
                System.out.println("\nList of member(s) currently reserved " + clientContext.getInventoryInstanceSelected() + ":");

                new HashSet<>(reservationsWithInventoryInstance.stream()
                        .filter(reservation -> reservation.getEndDate().after(Utility.getTodayDate()))
                        .map(reservation -> reservation.getMember())
                        .collect(Collectors.toMap(
                                member -> member.getId(),
                                member -> member,
                                (existing, replacement) -> existing
                        ))
                        .values())
                        .forEach(member -> System.out.println(member));
            }

            clientContext.clearOperationContext();
            clientContext.setClientState(new PendingContinueState(clientContext));
        }
    }
}
