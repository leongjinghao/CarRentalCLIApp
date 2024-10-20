package com.carrentalproj.repository;

import com.carrentalproj.entity.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepository {

    private final List<Member> members;
    private int memberId;

    public MemberRepositoryImpl() {
        members = new ArrayList<>();
        memberId = 1;
    }

    @Override
    public Member findById(int id) {
        return members.stream()
                .filter(member -> member.getId() == id).findFirst().get();
    }

    @Override
    public List<Member> findAll() {
        return members;
    }

    @Override
    public void save(Member member) {
        member.setId(memberId++);
        members.add(member);
    }

    @Override
    public void delete(int id) {
        members.stream()
                .filter(member -> member.getId() == id).findFirst()
                .ifPresent(member -> members.remove(member));
    }
}
