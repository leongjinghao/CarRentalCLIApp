package com.carrentalproj;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.service.RentalService;
import com.carrentalproj.service.RentalServiceImp;

import java.util.Calendar;

import static com.carrentalproj.Utility.prepopulateData;

public class App {

    public static void main(String[] args) throws InterruptedException {

        prepopulateData();

        Thread clientThread = new Thread(new Runnable() {
            public void run() {
                ClientContext carRentalClientContext = new ClientContext();
                carRentalClientContext.request();
            }
        });

        Thread dailyProcessThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Calendar nextDay = Calendar.getInstance();
                        nextDay.setTime(Utility.getTodayDate());
                        nextDay.add(Calendar.DAY_OF_MONTH, 1);
                        RentalService rentalService = RentalServiceImp.getInstance();

                        long timeToNextDay = nextDay.getTimeInMillis() - System.currentTimeMillis();
                        Thread.sleep(timeToNextDay);

                        rentalService.chargeLatePendingReturn();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        clientThread.start();

        dailyProcessThread.setDaemon(true);
        dailyProcessThread.start();

        clientThread.join();
    }
}
