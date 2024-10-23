package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Reservation;
import com.carrentalproj.exception.IllegalCarRentalOperationArgumentException;
import com.carrentalproj.service.ReservationService;

import java.util.Scanner;

public class CancelReservationState implements ClientState {

    private final ClientContext clientContext;

    public CancelReservationState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        ReservationService reservationService = clientContext.getRentalReservationOrchestrator();

        System.out.println("Starting Cancel Reservation Request...");
        System.out.println("Please enter the reservation ID to cancel:");
        int reservationId = sc.nextInt();

        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            reservationService.cancelReservation(clientContext.getCurrentMember().getId(), reservationId);
            System.out.println("Reservation: " + reservation + " canceled successfully");
        } catch (IllegalCarRentalOperationArgumentException e) {
            System.out.println(e.getMessage());
        }

        clientContext.setClientState(new PendingContinueState(clientContext));
    }
}
