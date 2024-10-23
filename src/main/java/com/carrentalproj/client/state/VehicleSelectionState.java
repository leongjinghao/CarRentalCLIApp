package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.vehicleType.Vehicle;
import com.carrentalproj.service.VehicleService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class VehicleSelectionState implements ClientState {

    private final ClientContext clientContext;

    public VehicleSelectionState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        Scanner sc = new Scanner(System.in);
        VehicleService vehicleService = clientContext.getVehicleService();
        List<Vehicle> vehicleTypes = vehicleService.getAllVehicleTypes();

        System.out.println("Select a vehicle type:");
        AtomicInteger option = new AtomicInteger(1);
        vehicleTypes.forEach(vehicle -> System.out.println(option.getAndIncrement() + ". " + vehicle.getBrand() + ": " + vehicle.getModel() + " (" + vehicle.getColour() + ")"));
        int optionSelected = sc.nextInt();

        clientContext.setVehicleTypeSelected(vehicleTypes.get(optionSelected - 1));
        clientContext.setClientState(new InventorySelectionState(clientContext));
    }
}
