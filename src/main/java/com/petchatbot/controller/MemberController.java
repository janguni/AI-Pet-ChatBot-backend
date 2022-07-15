package com.petchatbot.controller;

import com.petchatbot.domain.DTO.UserDto;
import com.petchatbot.domain.Member;
import com.petchatbot.repository.MemberRepository;
import com.petchatbot.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberServiceImpl userServiceImpl;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ResponseBody
    @PostMapping("/join")
    public String join(@RequestBody UserDto userDto){
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        log.info("email={}, password={}", email, password);
        Member member = new Member(email, password);

        // 패스워드 암호화
        String rawPassword = member.getMemberPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.changePassword(encPassword);

        // 회원가입
        userServiceImpl.join(member);

        List<Member> users = memberRepository.findAll();
        return "OK";
    }
}
