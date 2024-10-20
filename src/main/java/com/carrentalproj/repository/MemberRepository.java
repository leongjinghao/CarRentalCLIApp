package com.carrentalproj.repository;

import com.carrentalproj.entity.Member;

import java.sql.SQLException;
import java.util.List;

public interface MemberRepository {

    Member findById(int id);
    List<Member> findAll();
    int save(Member member) throws SQLException;
    void delete(int id);
}
