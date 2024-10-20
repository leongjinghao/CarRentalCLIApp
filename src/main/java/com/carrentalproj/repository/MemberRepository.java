package com.carrentalproj.repository;

import com.carrentalproj.entity.Member;

import java.util.List;

public interface MemberRepository {

    Member findById(int id);
    List<Member> findAll();
    void save(Member member);
    void delete(int id);
}
