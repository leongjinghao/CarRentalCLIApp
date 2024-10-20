package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;

public class StartState implements ClientState {

    private final ClientContext clientContext;

    public StartState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        if (clientContext.getCurrentMember() == null) {
            clientContext.setClientState(new SetMemberState(clientContext));
        } else {
            clientContext.setClientState(new ChoosingOperationState(clientContext));
        }
    }
}
