package com.carrentalproj.repository;

import com.carrentalproj.entity.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InventoryRepositoryImpl implements InventoryRepository {

    private final List<Inventory> inventoryStore;
    private int inventoryId;

    private final Lock lock;

    public InventoryRepositoryImpl() {
        inventoryStore = new ArrayList<>();
        inventoryId = 1;

        lock = new ReentrantLock();
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
        lock.lock();

        inventory.setId(inventoryId++);
        inventoryStore.add(inventory);

        lock.unlock();
    }

    @Override
    public void delete(int id) {
        lock.lock();

        inventoryStore.stream()
                .filter(inventory -> inventory.getId() == id).findFirst()
                .ifPresent(inventory -> inventoryStore.remove(inventory));

        lock.unlock();
    }
}
