package com.carrentalproj;

import com.carrentalproj.client.ClientContext;

import static com.carrentalproj.Utility.*;

public class App {

    public static void main(String[] args) throws InterruptedException {

        prepopulateData();

        Thread clientThread = new Thread(new Runnable() {
            public void run() {
                ClientContext carRentalClientContext = new ClientContext();
                carRentalClientContext.request();
            }
        });

        clientThread.start();
        clientThread.join();
    }
}
