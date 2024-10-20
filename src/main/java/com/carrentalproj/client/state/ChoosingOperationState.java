package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;

import java.util.Scanner;

public class ChoosingOperationState implements ClientState {

    private final ClientContext clientContext;

    public ChoosingOperationState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                
                Please select an operation to perform:
                1. Rent a Vehicle
                2. Reserve a Vehicle
                3. Return a Vehicle
                4. Cancel a Reservation
                5. List Vehicles by Member
                6. List Members by Vehicle
                7. Exit
                Enter the number corresponding to your choice:""");

        int operationSelected = sc.nextInt();

        switch (operationSelected) {
            case 1 -> clientContext.setClientState(new RentalState(this.clientContext));
            case 2 -> clientContext.setClientState(new ReservationState(this.clientContext));
            case 3 -> clientContext.setClientState(new ReturnedVehicleState(this.clientContext));
            case 4 -> clientContext.setClientState(new CancelReservationState(this.clientContext));
            // case 5:
            //     break;
            // case 6:
            //     break;
            // case 7:
            //     break;
            case 7 -> clientContext.setClientState(null);
            default -> {
                System.out.println("Invalid input... Please try again...");
                clientContext.setClientState(this);
            }
        }
    }
}
