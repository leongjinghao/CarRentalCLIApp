package com.carrentalproj.service;


import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Rental;

import java.util.Date;
import java.util.List;

public interface RentalService {

    List<Rental> getAllRentals();
    List<Rental> getRentalsByInventoryInstanceId(int inventoryInstanceId);
    List<Rental> getRentalsByMemberId(int memberId);
    Rental getRentalsById(int id);
    int rent(Inventory inventoryInstance, Member member, Date dueDate);
    void updateRentalReturnedStatus(int id, boolean isReturned);
    void returned(int id);
}
