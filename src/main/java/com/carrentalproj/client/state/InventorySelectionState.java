package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.exception.IllegalClientStateException;
import com.carrentalproj.service.InventoryService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class InventorySelectionState implements ClientState {

    private final ClientContext clientContext;

    public InventorySelectionState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        InventoryService inventoryService = clientContext.getInventoryService();
        List<Inventory> inventoryInstances = inventoryService.getAllFromInventoryByVehicleTypeId(
                clientContext.getVehicleTypeSelected().getId());

        System.out.println("Select a inventory instance:");
        AtomicInteger option = new AtomicInteger(1);
        inventoryInstances.forEach(inventoryInstance -> System.out.println(option.getAndIncrement() + ". " +
                "Parking Stall No.:" + inventoryInstance.getParkingStallNum() +
                ", Barcode: " + inventoryInstance.getBarcode() +
                ", Rate of rental: " + inventoryInstance.getRateOfRental() +
                " (" + inventoryInstance.getStatus() + ")"));

        int optionSelected = sc.nextInt();

        try {
            Inventory inventoryInstanceSelected = inventoryInstances.get(optionSelected - 1);
            clientContext.setInventoryInstanceSelected(inventoryInstanceSelected);

            switch (clientContext.getBusinessOperation()) {
                case "rental" -> clientContext.setClientState(new RentalState(clientContext));
                case "reservation" -> clientContext.setClientState(new ReservationState(clientContext));
                case "ViewMembersByInventoryInstance" ->
                        clientContext.setClientState(new ViewMembersByInventoryInstance(clientContext));
                default ->
                        throw new IllegalClientStateException("Illegal client state: Unexpected current business operation (" + clientContext.getBusinessOperation() + ")");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid selection: Please select an option within the list shown");
            clientContext.setClientState(new InventorySelectionState(clientContext));
        }
    }
}
