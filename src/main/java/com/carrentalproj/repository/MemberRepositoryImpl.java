package com.carrentalproj.repository;

import com.carrentalproj.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemberRepositoryImpl implements MemberRepository {

    private final List<Member> members;
    private int memberId;

    private final Lock lock;

    public MemberRepositoryImpl() {
        members = new ArrayList<>();
        memberId = 1;

        lock = new ReentrantLock();
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
        lock.lock();

        member.setId(memberId++);
        members.add(member);

        lock.unlock();
    }

    @Override
    public void delete(int id) {
        lock.lock();

        members.stream()
                .filter(member -> member.getId() == id).findFirst()
                .ifPresent(member -> members.remove(member));

        lock.unlock();
    }
}
