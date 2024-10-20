package com.carrentalproj.entity.vehicleType;

public class SUV extends Vehicle {

    public SUV() {
    }

    public SUV(int id, String brand, String model, String colour) {
        super(id, brand, model, colour);
    }

    @Override
    public void drive() {
        System.out.println("Driving a SUV...");
    }
}
