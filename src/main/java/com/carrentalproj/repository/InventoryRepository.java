package com.carrentalproj.repository;

import com.carrentalproj.entity.Inventory;

import java.sql.SQLException;
import java.util.List;

public interface InventoryRepository {

    Inventory findById(int id);
    List<Inventory> findAll();
    int save(Inventory inventory) throws SQLException;
    void delete(int id);
}
