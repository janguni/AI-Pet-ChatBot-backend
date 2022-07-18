package com.petchatbot.service;

import com.petchatbot.domain.dto.MemberDto;
import com.petchatbot.domain.model.Member;
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

    public boolean isEqualPassword(String password, String againPassword){
        if (password.equals(againPassword)){
            return true;
        }
        else{
            return false;
        }
    }

    public void join(MemberDto memberDto){
        //validateDuplicateMember(member);
        String email = memberDto.getEmail();
        String password = memberDto.getPassword();
        Member member = new Member(email, password);
        memberRepository.save(member);
    }

//    private void validateDuplicateMember(Member member) {
//        Member findMember = memberRepository.findByMemberEmail(member.getMemberEmail());
//
//        if (findMember != null){
//            throw new IllegalStateException("이미 가입된 회원입니다.");
//        }
//    }


}
