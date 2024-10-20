package com.carrentalproj;

import com.carrentalproj.entity.*;
import com.carrentalproj.repository.*;
import com.carrentalproj.service.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.carrentalproj.Utility.*;

public class TestApp {

    private static final Map<String, Integer> ids = new HashMap<>();

    public static void main(String[] args) {

        MemberService memberService = new MemberServiceImpl(new MemberRepositoryImpl());
        VehicleService vehicleService = new VehicleServiceImpl(new VehicleRepositoryImpl());
        InventoryService inventoryService = new InventoryServiceImpl(new InventoryRepositoryImpl());
        RentalService rentalService = new RentalServiceImp(new RentalRepositoryImpl());
        ReservationService reservationService = new ReservationServiceImpl(new ReservationRepositoryImpl());
        RentalReservationOrchestrator rentalReservationOrchestrator = new RentalReservationOrchestrator(rentalService, reservationService);

        /*
         * Initialise sample member instances
         */
        prepopulateSampleMemberInstances(memberService, ids);

        /*
         * Initialise sample vehicle models that are available in the rental service
         */
        prepopulateSampleVehicleModels(vehicleService, ids);

        /*
         * Initialise sample inventory instance, of vehicle models available in the rental service
         */
        prepopulateSampleInventoryInstances(vehicleService, inventoryService, ids);

        /*
         * Sample rental & reservation requests
         */

        // John's transactions
        Member john = memberService.getMember(ids.get("johnMemberId"));
        Inventory teslaModel3Instance2 = inventoryService.getFromInventory(ids.get("teslaModel3InventoryInstance2Id"));
        Inventory toyotaHiaceInstance1 = inventoryService.getFromInventory(ids.get("toyotaHiaceInstance1Id"));

        int johnRentId1 = rentalReservationOrchestrator.rent(teslaModel3Instance2, john, new Date(2024, Calendar.OCTOBER, 20));
        int johnResId1 = rentalReservationOrchestrator.reserve(teslaModel3Instance2, john, new Date(2024, Calendar.OCTOBER, 21), new Date(2024, Calendar.OCTOBER, 25));
        int johnRentId2 = rentalReservationOrchestrator.rent(toyotaHiaceInstance1, john, new Date(2024, Calendar.NOVEMBER, 10));

        rentalReservationOrchestrator.cancelReservation(johnResId1);

        // Anna's transactions
        Member anna = memberService.getMember(ids.get("annaMemberId"));

        int annaResId1 = rentalReservationOrchestrator.reserve(teslaModel3Instance2, anna, new Date(2024, Calendar.OCTOBER, 26), new Date(2024, Calendar.OCTOBER, 30));
        Inventory teslaModel3Instance1 = inventoryService.getFromInventory(ids.get("teslaModel3InventoryInstance1Id"));
        int annaRentId1 = rentalReservationOrchestrator.rent(teslaModel3Instance1, anna, new Date(2024, Calendar.OCTOBER, 21));

        rentalReservationOrchestrator.cancelReservation(annaResId1);
        int annaResId2 = rentalReservationOrchestrator.reserve(teslaModel3Instance2, anna, new Date(2024, Calendar.OCTOBER, 22), new Date(2024, Calendar.OCTOBER, 30));

        rentalReservationOrchestrator.returned(johnRentId2);
        int annaResId3 = rentalReservationOrchestrator.reserve(toyotaHiaceInstance1, anna, new Date(2024, Calendar.OCTOBER, 22), new Date(2024, Calendar.OCTOBER, 30));
    }
}
