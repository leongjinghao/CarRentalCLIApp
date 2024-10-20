package com.carrentalproj.entity;

import com.carrentalproj.Utility;

import java.util.Date;

public class Rental {

    private int id;
    private Date startDate;
    private Date dueDate;
    private double rentalFee;
    private double lateFee;
    private boolean isReturned;

    // private int inventoryId;
    private Inventory inventoryInstance;

    // private int memberId;
    private Member member;

    public Rental() {
    }

    public Rental(int id, Inventory inventoryInstance, Member member, Date dueDate, double rentalFee) {
        this.id = id;
        this.inventoryInstance = inventoryInstance;
        this.member = member;
        this.startDate = Utility.getTodayDate();
        this.dueDate = dueDate;
        this.rentalFee = rentalFee;
        this.lateFee = 0.0;
        this.isReturned = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public Inventory getInventoryInstance() {
        return inventoryInstance;
    }

    public void setInventoryInstance(Inventory inventoryInstance) {
        this.inventoryInstance = inventoryInstance;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", dueDate=" + dueDate +
                ", rentalFee=" + rentalFee +
                ", lateFee=" + lateFee +
                ", isReturned=" + isReturned +
                ", inventoryInstance=" + inventoryInstance +
                ", member=" + member +
                '}';
    }
}
