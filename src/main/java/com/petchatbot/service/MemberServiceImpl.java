package com.petchatbot.service;

import com.petchatbot.domain.Member;
import com.petchatbot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    // 기존 회원인지 체크
    public boolean isExistingMember(String email){
        Member findMember = memberRepository.findByMemberEmail(email);

        if (findMember == null){
            return false;
        }
        return true;
    }

    public void join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByMemberEmail(member.getMemberEmail());

        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


}
