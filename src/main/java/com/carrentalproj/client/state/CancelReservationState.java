package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Reservation;
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

        Reservation reservation = reservationService.getReservationById(reservationId);
        reservationService.cancelReservation(reservationId);
        System.out.println("Reservation: " + reservation + " canceled successfully");

        clientContext.setClientState(new StartState(clientContext));
    }
}
