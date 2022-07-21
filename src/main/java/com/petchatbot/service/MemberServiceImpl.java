package com.petchatbot.service;

import com.petchatbot.domain.dto.MemberDto;
import com.petchatbot.domain.model.Member;
import com.petchatbot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        validateDuplicateMember(memberDto);
        String email = memberDto.getEmail();
        String password = memberDto.getPassword();
        Member member = new Member(email, password);
        memberRepository.save(member);
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {

    }

    private void validateDuplicateMember(MemberDto memberDto) {
        Member findMember = memberRepository.findByMemberEmail(memberDto.getEmail());

        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


}
