package com.carrentalproj.client.state;

import com.carrentalproj.Utility;
import com.carrentalproj.client.ClientContext;
import com.carrentalproj.service.ReservationService;

import java.util.Date;
import java.util.Scanner;

public class ReservationState implements ClientState {

    private final ClientContext clientContext;

    public ReservationState(ClientContext clientContext) {
        this.clientContext = clientContext;
        clientContext.setBusinessOperation("reservation");
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        ReservationService reservationService = clientContext.getRentalReservationOrchestrator();

        if (clientContext.getVehicleTypeSelected() == null || clientContext.getInventoryInstanceSelected() == null) {
            System.out.println("Starting Reservation Operation...");
            clientContext.setClientState(new VehicleSelectionState(clientContext));
        }
        else {
            System.out.println("Please enter a start and end date for the reservation:");
            System.out.println("Start date (DD/MM/YYYY):");
            String startDateStr = sc.nextLine();
            System.out.println("End date (DD/MM/YYYY):");
            String endDateStr = sc.nextLine();

            String[] startDateStrArr = startDateStr.split("/");
            Date startDate = Utility.getDate(
                    Integer.parseInt(startDateStrArr[0]),
                    Integer.parseInt(startDateStrArr[1]),
                    Integer.parseInt(startDateStrArr[2])
            );

            String[] endDateStrArr = endDateStr.split("/");
            Date endDate = Utility.getDate(
                    Integer.parseInt(endDateStrArr[0]),
                    Integer.parseInt(endDateStrArr[1]),
                    Integer.parseInt(endDateStrArr[2])
            );

            try {
                int reservationId = reservationService.reserve(
                        clientContext.getInventoryInstanceSelected(),
                        clientContext.getCurrentMember(),
                        startDate,
                        endDate
                );
                System.out.println("Reservation successful, reservation ID: " + reservationId);

                clearOperationContext();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());

                System.out.println("Retry with another date? (yes/no):");
                String retry = sc.nextLine();

                if (retry.equals("yes")) {
                    clientContext.setClientState(new ReservationState(clientContext));
                } else {
                    clearOperationContext();
                }
            }
        }
    }

    private void clearOperationContext() {
        clientContext.setVehicleTypeSelected(null);
        clientContext.setInventoryInstanceSelected(null);
        clientContext.setBusinessOperation("");
        clientContext.setClientState(new StartState(clientContext));
    }
}
