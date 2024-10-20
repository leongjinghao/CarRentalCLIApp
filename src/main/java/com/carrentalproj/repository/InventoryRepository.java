package com.carrentalproj.repository;

import com.carrentalproj.entity.Inventory;

import java.util.List;

public interface InventoryRepository {

    Inventory findById(int id);
    List<Inventory> findAll();
    void save(Inventory inventory);
    void delete(int id);
}
