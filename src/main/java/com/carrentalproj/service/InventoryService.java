package com.carrentalproj.service;

import com.carrentalproj.entity.Inventory;

import java.util.List;

public interface InventoryService {

    List<Inventory> getAllFromInventory();
    List<Inventory> getAllFromInventoryByVehicleTypeId(int vehicleTypeId);
    int addToInventory(Inventory inventory);
    Inventory getFromInventory(int id);
    void updateInventoryStatus(int id, String status);
}
