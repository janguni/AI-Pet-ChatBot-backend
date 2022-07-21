package com.petchatbot.service;

import com.petchatbot.domain.dto.MemberDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MemberService {


    // 기존 회원인지 체크
    boolean isExistingMember(String email);

    // 회원가입
    void join(MemberDto memberDto);

    // 로그인 실패
    void loginFail(HttpServletRequest request, HttpServletResponse response);



}
