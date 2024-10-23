package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;

import java.io.IOException;

public class PendingContinueState implements ClientState {

    private final ClientContext clientContext;

    public PendingContinueState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        System.out.println("Press enter key to continue...");

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clientContext.setClientState(new StartState(clientContext));

    }
}
