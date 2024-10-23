package com.carrentalproj;

import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.vehicleType.Car;
import com.carrentalproj.entity.vehicleType.Van;
import com.carrentalproj.entity.vehicleType.Vehicle;
import com.carrentalproj.exception.IllegalCarRentalOperationArgumentException;
import com.carrentalproj.service.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utility {

    public static Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, getCalendarConstant(month), day);
        return calendar.getTime();
    }

    public static int getCalendarConstant(int month) {
        return switch (month) {
            case 1 -> Calendar.JANUARY;
            case 2 -> Calendar.FEBRUARY;
            case 3 -> Calendar.MARCH;
            case 4 -> Calendar.APRIL;
            case 5 -> Calendar.MAY;
            case 6 -> Calendar.JUNE;
            case 7 -> Calendar.JULY;
            case 8 -> Calendar.AUGUST;
            case 9 -> Calendar.SEPTEMBER;
            case 10 -> Calendar.OCTOBER;
            case 11 -> Calendar.NOVEMBER;
            case 12 -> Calendar.DECEMBER;
            default -> throw new IllegalCarRentalOperationArgumentException("Invalid month: " + month);
        };
    }

    public static void prepopulateData() {
        Map<String, Integer> ids = new HashMap<>();

        prepopulateSampleMemberInstances(ids);
        prepopulateSampleVehicleModels(ids);
        prepopulateSampleInventoryInstances(ids);
    }

    public static void prepopulateSampleMemberInstances(Map<String, Integer> ids) {
        MemberService memberService = MemberServiceImpl.getInstance();

        Member john = new Member(0, "John" , "Hunk");
        Member anna = new Member(0, "Anna", "Mary");
        int johnMemberId = memberService.addMember(john);
        int annaMemberId = memberService.addMember(anna);

        ids.put("johnMemberId", johnMemberId);
        ids.put("annaMemberId", annaMemberId);
    }

    public static void prepopulateSampleVehicleModels(Map<String, Integer> ids) {
        VehicleService vehicleService = VehicleServiceImpl.getInstance();

        Vehicle teslaModel3 = new Car(0, "Tesla", "Model 3", "Black");
        Vehicle toyotaHiace = new Van(0, "Toyota","Hiace", "white");
        int teslaModel3VehicleId = vehicleService.addVehicleType(teslaModel3);
        int toyotaHiaceVehicleId = vehicleService.addVehicleType(toyotaHiace);

        ids.put("teslaModel3VehicleId", teslaModel3VehicleId);
        ids.put("toyotaHiaceVehicleId", toyotaHiaceVehicleId);
    }

    public static void prepopulateSampleInventoryInstances(Map<String, Integer> ids) {
        VehicleService vehicleService = VehicleServiceImpl.getInstance();
        InventoryService inventoryService = InventoryServiceImpl.getInstance();

        Vehicle teslaModel3 = vehicleService.getVehicleType(ids.get("teslaModel3VehicleId"));
        Vehicle toyotaHiace = vehicleService.getVehicleType(ids.get("toyotaHiaceVehicleId"));

        Inventory teslaModel3Instance1 = new Inventory(0, teslaModel3, "barcode123", "lot11", 20.50, "available");
        Inventory teslaModel3Instance2 = new Inventory(0, teslaModel3, "barcode124", "lot12", 25.50, "available");
        Inventory toyotaHiaceInstance1 = new Inventory(0, toyotaHiace, "barcode987", "lot52", 9.20, "available");
        int teslaModel3InventoryInstance1Id = inventoryService.addToInventory(teslaModel3Instance1);
        int teslaModel3InventoryInstance2Id = inventoryService.addToInventory(teslaModel3Instance2);
        int toyotaHiaceInstance1Id = inventoryService.addToInventory(toyotaHiaceInstance1);

        ids.put("teslaModel3InventoryInstance1Id", teslaModel3InventoryInstance1Id);
        ids.put("teslaModel3InventoryInstance2Id", teslaModel3InventoryInstance2Id);
        ids.put("toyotaHiaceInstance1Id", toyotaHiaceInstance1Id);
    }
}
