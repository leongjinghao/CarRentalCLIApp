package com.carrentalproj.client.state;

import com.carrentalproj.Utility;
import com.carrentalproj.client.ClientContext;
import com.carrentalproj.exception.IllegalCarRentalOperationArgumentException;
import com.carrentalproj.service.RentalService;

import java.util.Date;
import java.util.Scanner;

public class RentalState implements ClientState {

    private final ClientContext clientContext;

    public RentalState(ClientContext clientContext) {
        this.clientContext = clientContext;
        clientContext.setBusinessOperation("rental");
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        RentalService rentalService = clientContext.getRentalReservationOrchestrator();

        if (clientContext.getVehicleTypeSelected() == null || clientContext.getInventoryInstanceSelected() == null) {
            System.out.println("Starting Rental Operation...");
            clientContext.setClientState(new VehicleSelectionState(clientContext));
        } else {
            System.out.println("Rental will start from today, please enter a due date (DD/MM/YYYY): ");
            String dateStr = sc.nextLine();
            String[] dateStrArr = dateStr.split("/");
            Date dueDate = Utility.getDate(
                    Integer.parseInt(dateStrArr[0]),
                    Integer.parseInt(dateStrArr[1]),
                    Integer.parseInt(dateStrArr[2])
            );

            try {
                int rentalId = rentalService.rent(
                        clientContext.getInventoryInstanceSelected(),
                        clientContext.getCurrentMember(),
                        dueDate
                );
                System.out.println("Rental successful, rental ID: " + rentalId);
            } catch (IllegalCarRentalOperationArgumentException e) {
                System.out.println(e.getMessage());
            }

            clientContext.clearOperationContext();
            clientContext.setClientState(new PendingContinueState(clientContext));
        }
    }
}
