package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Member;
import com.carrentalproj.service.MemberService;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class SetMemberState implements ClientState {

    private final ClientContext clientContext;

    public SetMemberState(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleRequest() {

        MemberService memberService = clientContext.getMemberService();
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter your Member ID: ");
        int memberId = sc.nextInt();

        try {
            Member currentMember = memberService.getMember(memberId);

            System.out.println("\nWelcome, " + currentMember.getFirstName() + "!");

            clientContext.setCurrentMember(currentMember);
            clientContext.setClientState(new PendingContinueState(clientContext));
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + ", please enter a valid Member ID...");
            System.out.println("* note: Member ID 1 (John) & 2 (Anna) are available if prepopulateData() was executed on App.java\n");
            clientContext.setClientState(new SetMemberState(clientContext));
        }
    }
}
