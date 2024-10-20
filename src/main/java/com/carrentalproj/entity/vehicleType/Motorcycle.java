package com.carrentalproj.entity.vehicleType;

public class Motorcycle extends Vehicle {

    public Motorcycle() {
    }

    public Motorcycle(int id, String brand, String model, String colour) {
        super(id, brand, model, colour);
    }

    @Override
    public void drive() {
        System.out.println("Riding a motorcycle...");
    }
}
