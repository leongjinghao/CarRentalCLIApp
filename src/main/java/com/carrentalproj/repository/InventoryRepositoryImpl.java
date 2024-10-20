package com.carrentalproj.repository;

import com.carrentalproj.entity.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryRepositoryImpl implements InventoryRepository {

    private final List<Inventory> inventoryStore;
    private int inventoryId;

    public InventoryRepositoryImpl() {
        inventoryStore = new ArrayList<>();
        inventoryId = 1;
    }

    @Override
    public Inventory findById(int id) {
        return inventoryStore.stream()
                .filter(inventory -> inventory.getId() == id).findFirst().get();
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryStore;
    }

    @Override
    public void save(Inventory inventory) {
        inventory.setId(inventoryId++);
        inventoryStore.add(inventory);
    }

    @Override
    public void delete(int id) {
        inventoryStore.stream()
                .filter(inventory -> inventory.getId() == id).findFirst()
                .ifPresent(inventory -> inventoryStore.remove(inventory));
    }
}
