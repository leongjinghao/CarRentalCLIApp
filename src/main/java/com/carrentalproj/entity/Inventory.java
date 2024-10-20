package com.carrentalproj.entity;

import com.carrentalproj.entity.vehicleType.Vehicle;

public class Inventory {

    private int id;
    private String barcode;
    private String parkingStallNum;
    private double rateOfRental;
    private String status;

    // private int vehicleId;
    private Vehicle vehicle;

    public Inventory() {
    }

    public Inventory(int id, Vehicle vehicle, String barcode, String parkingStallNum, double rateOfRental, String status) {
        this.id = id;
        this.vehicle = vehicle;
        this.barcode = barcode;
        this.parkingStallNum = parkingStallNum;
        this.rateOfRental = rateOfRental;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getParkingStallNum() {
        return parkingStallNum;
    }

    public void setParkingStallNum(String parkingStallNum) {
        this.parkingStallNum = parkingStallNum;
    }

    public double getRateOfRental() {
        return rateOfRental;
    }

    public void setRateOfRental(double rateOfRental) {
        this.rateOfRental = rateOfRental;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", vehicle='" + vehicle + '\'' +
                ", barcode='" + barcode + '\'' +
                ", parkingStallNum='" + parkingStallNum + '\'' +
                ", rateOfRental=" + rateOfRental +
                ", status='" + status + '\'' +
                '}';
    }
}
