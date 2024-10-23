package com.carrentalproj.service;

import com.carrentalproj.entity.Inventory;
import com.carrentalproj.entity.Member;
import com.carrentalproj.entity.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationService {

    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByInventoryInstanceId(int inventoryInstanceId);
    List<Reservation> getReservationsByMemberId(int memberId);
    Reservation getReservationById(int id);
    int reserve(Inventory inventoryInstance, Member member, Date startDate, Date endDate);
    void cancelReservation(int memberId, int reservationId);
}
