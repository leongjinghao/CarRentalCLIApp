package com.carrentalproj.entity.vehicleType;

public class Vehicle {

    private int id;
    private String brand;
    private String model;
    private String colour;

    public Vehicle() {
    }

    public Vehicle(int id, String brand, String model, String colour) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void drive() {
        System.out.println("To be implemented by subclass");
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                '}';
    }
}
