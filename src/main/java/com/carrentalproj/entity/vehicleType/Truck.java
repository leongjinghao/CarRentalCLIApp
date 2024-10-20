package com.carrentalproj.entity.vehicleType;

public class Truck extends Vehicle {

    public Truck() {
    }

    public Truck(int id, String brand, String model, String colour) {
        super(id, brand, model, colour);
    }

    @Override
    public void drive() {
        System.out.println("Driving a truck...");
    }
}
