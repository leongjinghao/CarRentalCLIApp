package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Rental;
import com.carrentalproj.exception.IllegalCarRentalOperationArgumentException;
import com.carrentalproj.service.RentalService;

import java.util.Scanner;

public class ReturnedVehicleState implements ClientState {

    private final ClientContext clientContext;

    public ReturnedVehicleState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        RentalService rentalService = clientContext.getRentalReservationOrchestrator();

        System.out.println("Starting Rental Returned Request...");
        System.out.println("Please enter the rental ID to return:");
        int rentalId = sc.nextInt();

        try {
            Rental rental = rentalService.getRentalsById(rentalId);
            rentalService.returned(clientContext.getCurrentMember().getId(), rentalId);
            System.out.println("Rental: " + rental + " returned successfully");
        } catch (IllegalCarRentalOperationArgumentException e) {
            System.out.println(e.getMessage());
        }

        clientContext.setClientState(new PendingContinueState(clientContext));
    }
}
