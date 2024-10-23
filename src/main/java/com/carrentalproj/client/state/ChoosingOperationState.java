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
                5. List the Vehicle(s) Rented or Reserved by the Selected Member
                6. List the Member(s) Who Currently Rented or Reserved the Selected Vehicle
                7. Change Current Member
                8. Exit
                Enter the number corresponding to your choice:""");

        int operationSelected = sc.nextInt();

        switch (operationSelected) {
            case 1 -> clientContext.setClientState(new RentalState(clientContext));
            case 2 -> clientContext.setClientState(new ReservationState(clientContext));
            case 3 -> clientContext.setClientState(new ReturnedVehicleState(clientContext));
            case 4 -> clientContext.setClientState(new CancelReservationState(clientContext));
            case 5 -> clientContext.setClientState(new ViewInventoryInstancesByMemberState(clientContext));
            case 6 -> clientContext.setClientState(new ViewMembersByInventoryInstance(clientContext));
            case 7 -> clientContext.setClientState(new SetMemberState(clientContext));
            case 8 -> clientContext.setClientState(null);
            default -> {
                System.out.println("Invalid input... Please try again...");
                clientContext.setClientState(this);
            }
        }
    }
}
