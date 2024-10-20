package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Inventory;
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

        System.out.println("Select the inventory instance to rent:");
        AtomicInteger option = new AtomicInteger(1);
        inventoryInstances.forEach(inventoryInstance -> System.out.println(option.getAndIncrement() + ". " +
                "Parking Stall No.:" + inventoryInstance.getParkingStallNum() +
                ", Barcode: " + inventoryInstance.getBarcode() +
                ", Rate of rental: " + inventoryInstance.getRateOfRental() +
                "(" + inventoryInstance.getStatus() + ")"));

        int optionSelected = sc.nextInt();
        clientContext.setInventoryInstanceSelected(inventoryInstances.get(optionSelected - 1));

        if (clientContext.getBusinessOperation().equals("rental")) {
            clientContext.setClientState(new RentalState(clientContext));
        } else if (clientContext.getBusinessOperation().equals("reservation")) {
            clientContext.setClientState(new ReservationState(clientContext));
        }
    }
}
