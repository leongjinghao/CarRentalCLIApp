package com.carrentalproj.client;

import com.carrentalproj.client.state.ClientState;
import com.carrentalproj.client.state.StartState;
import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.vehicleType.Vehicle;
import com.carrentalproj.service.*;


public class ClientContext {

    private ClientState clientState;
    private Member currentMember;

    private Vehicle vehicleTypeSelected;
    private Inventory inventoryInstanceSelected;
    private String businessOperation;

    MemberService memberService;
    VehicleService vehicleService;
    InventoryService inventoryService;
    RentalReservationOrchestrator rentalReservationOrchestrator;


    public ClientContext(MemberService memberService, VehicleService vehicleService, InventoryService inventoryService, RentalReservationOrchestrator rentalReservationOrchestrator) {
        this.memberService = memberService;
        this.vehicleService = vehicleService;
        this.inventoryService = inventoryService;
        this.rentalReservationOrchestrator = rentalReservationOrchestrator;

        clientState = new StartState(this);
    }

    public void request() {
        while (clientState != null) {
            clientState.handleRequest();
        }
    }

    public ClientState getClientState() {
        return clientState;
    }

    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }

    public Member getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(Member currentMember) {
        this.currentMember = currentMember;
    }

    public Vehicle getVehicleTypeSelected() {
        return vehicleTypeSelected;
    }

    public void setVehicleTypeSelected(Vehicle vehicleTypeSelected) {
        this.vehicleTypeSelected = vehicleTypeSelected;
    }

    public Inventory getInventoryInstanceSelected() {
        return inventoryInstanceSelected;
    }

    public void setInventoryInstanceSelected(Inventory inventoryInstanceSelected) {
        this.inventoryInstanceSelected = inventoryInstanceSelected;
    }

    public String getBusinessOperation() {
        return businessOperation;
    }

    public void setBusinessOperation(String businessOperation) {
        this.businessOperation = businessOperation;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public RentalReservationOrchestrator getRentalReservationOrchestrator() {
        return rentalReservationOrchestrator;
    }

    public void setRentalReservationOrchestrator(RentalReservationOrchestrator rentalReservationOrchestrator) {
        this.rentalReservationOrchestrator = rentalReservationOrchestrator;
    }
}
