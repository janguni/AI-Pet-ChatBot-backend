package com.petchatbot.repository;


import com.petchatbot.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    public Member findByMemberEmail(String memberEmail);

}