package com.carrentalproj.entity.vehicleType;

public class Van extends Vehicle {

    public Van() {
    }

    public Van(int id, String brand, String model, String colour) {
        super(id, brand, model, colour);
    }

    @Override
    public void drive() {
        System.out.println("Driving a van...");
    }
}
