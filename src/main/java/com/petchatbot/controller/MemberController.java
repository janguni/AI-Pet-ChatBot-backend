package com.petchatbot.controller;

import com.petchatbot.config.ResponseMessage;
import com.petchatbot.config.StatusCode;
import com.petchatbot.domain.dto.EmailCodeDto;
import com.petchatbot.domain.dto.EmailDto;
import com.petchatbot.domain.requestAndResponse.JoinReq;
import com.petchatbot.domain.dto.MemberDto;
import com.petchatbot.domain.requestAndResponse.DefaultRes;
import com.petchatbot.repository.MemberRepository;
import com.petchatbot.service.EmailService;
import com.petchatbot.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Random;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final EmailService emailService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinReq joinReq) {
        String email = joinReq.getMemberEmail();
        String password = joinReq.getMemberPassword();


        // 이메일이 이미 있는 경우
        if (memberService.isExistingMember(email)){
            return new ResponseEntity(DefaultRes.res(StatusCode.CONFLICTPERMALINK, ResponseMessage.DUPLICATE_USER), HttpStatus.OK);
        }

        String rawPassword = joinReq.getMemberPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword); // 패스워드 암호화

        // 성공 로직
        MemberDto memberDto = new MemberDto(email, encodedPassword);
        log.info("email={}, password={}", email, rawPassword);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.ENTER_JOIN_INFORMATION), HttpStatus.OK);
    }

    // 이메일 전송
    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) throws MessagingException {
        try {
            log.info("memberEmail={}", emailDto);
            int RandomNumber = makeRandomNumber();

            emailService.sendEmail(emailDto, "멍냥챗봇 인증번호 발송 이메일 입니다.", RandomNumber);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SEND_EMAIL, RandomNumber), HttpStatus.OK);

        } catch (MessagingException e){
            return new ResponseEntity(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.SEND_EMAIL_FAIL, null), HttpStatus.OK);
        }
    }


    // 인증번호 입력 (회원가입)
    @PostMapping("/enterEmailCode/join")
    public ResponseEntity<String> enterEmailCode(@RequestBody EmailCodeDto ecCode){
        log.info("enterEmailCode memberEmail={}, memberPassword={}, sendCode={}, receivedCode={}",
                ecCode.getMemberEmail(), ecCode.getMemberPassword(), ecCode.getSendCode(), ecCode.getReceivedCode());
        int sendCode = ecCode.getSendCode();
        int receivedCode = ecCode.getReceivedCode();

        if (sendCode == receivedCode){
            String memberEmail = ecCode.getMemberEmail();
            String rawPassword = ecCode.getMemberPassword();
            String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

            MemberDto memberDto = new MemberDto(memberEmail, encodedPassword);
            memberService.join(memberDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_USER), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.WRONG_EMAIL_CODE), HttpStatus.OK);
        }
    }

    // 인증코드 입력 (비밀번호 변경)
    @PostMapping("/enterEmailCode/changePw")
    public ResponseEntity<String> enterEmailCode_password(@RequestBody EmailCodeDto ecCode){
        int sendCode = ecCode.getSendCode();
        int receivedCode = ecCode.getReceivedCode();
        String memberEmail = ecCode.getMemberEmail();
        String memberPassword = ecCode.getMemberPassword();
        if (sendCode == receivedCode){
            MemberDto memberDto = new MemberDto(memberEmail, memberPassword);
            memberService.changePassword(memberDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CHANGE_PW), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.CHANGE_PW_FAIL), HttpStatus.OK);
        }
    }

    public int makeRandomNumber() {
        // 난수의 범위 111111 ~ 999999 (6자리 난수)
        Random r = new Random();
        int checkNum = r.nextInt(888888) + 111111;
        return checkNum;
    }



}

