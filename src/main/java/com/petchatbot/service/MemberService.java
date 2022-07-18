package com.petchatbot.service;

import com.petchatbot.domain.dto.MemberDto;

public interface MemberService {


    // 기존 회원인지 체크
    boolean isExistingMember(String email);

    // 입력한 비밀번호와 다시 입력한 비밀번호가 같은지 체크
    boolean isEqualPassword(String password, String againPassword);
    // 회원가입
    void join(MemberDto memberDto);



}
