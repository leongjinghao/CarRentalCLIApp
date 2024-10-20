package com.carrentalproj.entity;

import java.util.Date;

public class Reservation {

    private int id;
    private Date startDate;
    private Date endDate;

    // private int inventoryId;
    private Inventory inventoryInstance;

    // private int memberId;
    private Member member;

    public Reservation() {
    }

    public Reservation(int id, Inventory inventoryInstance, Member member, Date startDate, Date endDate) {
        this.id = id;
        this.inventoryInstance = inventoryInstance;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        return "Reservation{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", inventoryInstance=" + inventoryInstance +
                ", member=" + member +
                '}';
    }
}
