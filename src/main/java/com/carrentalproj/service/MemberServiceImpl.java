package com.carrentalproj.service;

import com.carrentalproj.entity.Member;
import com.carrentalproj.repository.MemberRepository;
import com.carrentalproj.repository.MemberRepositoryImpl;

import java.util.List;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private static MemberServiceImpl instance;

    private MemberServiceImpl() {
        memberRepository = MemberRepositoryImpl.getInstance();
    }

    public static synchronized MemberServiceImpl getInstance() {
        if (instance == null) {
            instance = new MemberServiceImpl();
        }

        return instance;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member getMember(int id) {
        return memberRepository.findById(id);
    }

    @Override
    public int addMember(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public void removeMember(int id) {
        memberRepository.delete(id);
    }
}
