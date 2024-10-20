package com.carrentalproj.entity.vehicleType;

public class Car extends Vehicle {

    public Car() {
    }

    public Car(int id, String brand, String model, String colour) {
        super(id, brand, model, colour);
    }

    @Override
    public void drive() {
        System.out.println("Driving a car...");
    }
}
