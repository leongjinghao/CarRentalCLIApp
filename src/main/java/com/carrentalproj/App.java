package com.carrentalproj;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.repository.*;
import com.carrentalproj.service.*;

import static com.carrentalproj.Utility.*;

public class App {

    public static void main(String[] args) throws InterruptedException {

        MemberService memberService = new MemberServiceImpl(new MemberRepositoryImpl());
        VehicleService vehicleService = new VehicleServiceImpl(new VehicleRepositoryImpl());
        InventoryService inventoryService = new InventoryServiceImpl(new InventoryRepositoryImpl());
        RentalService rentalService = new RentalServiceImp(new RentalRepositoryImpl());
        ReservationService reservationService = new ReservationServiceImpl(new ReservationRepositoryImpl());
        RentalReservationOrchestrator rentalReservationOrchestrator = new RentalReservationOrchestrator(rentalService, reservationService);

        prepopulateData(memberService, vehicleService, inventoryService);

        Thread clientThread = new Thread(new Runnable() {
            public void run() {
                ClientContext carRentalClientContext = new ClientContext(memberService, vehicleService, inventoryService, rentalReservationOrchestrator);
                carRentalClientContext.request();
            }
        });

        clientThread.start();
        clientThread.join();
    }
}
