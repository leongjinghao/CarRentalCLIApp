package com.carrentalproj.service;

import com.carrentalproj.entity.Member;

import java.util.List;

public interface MemberService {

    List<Member> getAllMembers();
    Member getMember(int id);
    int addMember(Member member);
    void updateMember(Member member);
    void removeMember(int id);
}
