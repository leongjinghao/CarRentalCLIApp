package com.carrentalproj.service;

import com.carrentalproj.entity.Member;
import com.carrentalproj.repository.MemberRepository;

import java.util.List;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
