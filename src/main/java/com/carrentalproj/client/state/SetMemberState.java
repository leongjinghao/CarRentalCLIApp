package com.carrentalproj.client.state;

import com.carrentalproj.client.ClientContext;
import com.carrentalproj.entity.Member;
import com.carrentalproj.service.MemberService;

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
        Member currentMember = memberService.getMember(memberId);

        System.out.println("\nWelcome, " + currentMember.getFirstName() + "!");

        clientContext.setCurrentMember(currentMember);
        clientContext.setClientState(new PendingContinueState(clientContext));
    }
}
