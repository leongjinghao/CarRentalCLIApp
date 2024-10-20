package com.carrentalproj.service;

import com.carrentalproj.entity.Inventory;
import com.carrentalproj.repository.InventoryRepository;

import java.util.List;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAllFromInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<Inventory> getAllFromInventoryByVehicleTypeId(int vehicleTypeId) {
        return inventoryRepository.findAll().stream()
                .filter(inventoryInstance -> inventoryInstance.getVehicle().getId() == vehicleTypeId).toList();
    }

    @Override
    public int addToInventory(Inventory inventoryInstance) {
        inventoryRepository.save(inventoryInstance);
        return inventoryInstance.getId();
    }

    @Override
    public Inventory getFromInventory(int id) {
        return inventoryRepository.findById(id);
    }
}
