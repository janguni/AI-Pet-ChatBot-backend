package com.petchatbot.controller;

import com.petchatbot.config.ResponseMessage;
import com.petchatbot.config.StatusCode;
import com.petchatbot.domain.requestAndResponse.JoinReq;
import com.petchatbot.domain.dto.MemberDto;
import com.petchatbot.domain.requestAndResponse.DefaultRes;
import com.petchatbot.repository.MemberRepository;
import com.petchatbot.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberServiceImpl memberServiceImpl;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinReq joinReq) {
        String email = joinReq.getEmail();
        String rawPassword = joinReq.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword); // 패스워드 암호화

        // 성공 로직
        MemberDto memberDto = new MemberDto(email, encodedPassword);
        log.info("email={}, password={}", email, rawPassword);
        memberServiceImpl.join(memberDto);

        // 인증 메일 발송

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SEND_EMAIL), HttpStatus.OK);
    }

}

