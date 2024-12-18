package com.carrentalproj.service;

import com.carrentalproj.entity.Inventory;
import com.carrentalproj.repository.InventoryRepository;
import com.carrentalproj.repository.InventoryRepositoryImpl;

import java.util.List;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private static InventoryServiceImpl instance;

    private InventoryServiceImpl() {
        inventoryRepository = InventoryRepositoryImpl.getInstance();
    }

    public static synchronized InventoryServiceImpl getInstance() {
        if (instance == null) {
            instance = new InventoryServiceImpl();
        }

        return instance;
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
    public int addToInventory(Inventory inventory) {
        try {
            return inventoryRepository.save(inventory);
        } catch (java.sql.SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public Inventory getFromInventory(int id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public void updateInventoryStatus(int id, String status) {
        try {
            Inventory inventory = inventoryRepository.findById(id);
            inventory.setStatus(status);
            inventoryRepository.save(inventory);
        } catch (java.sql.SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
