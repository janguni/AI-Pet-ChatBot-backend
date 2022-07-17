package com.petchatbot.controller;

import com.petchatbot.config.ResponseMessage;
import com.petchatbot.config.StatusCode;
import com.petchatbot.domain.DTO.MemberDto;
import com.petchatbot.domain.DefaultRes;
import com.petchatbot.domain.Member;
import com.petchatbot.repository.MemberRepository;
import com.petchatbot.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 이메일 중복확인 후 회원가입
     * @param memberDto
     * @param againPassWord
     * @return
     */

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberDto memberDto, @RequestParam("againPassword") String againPassWord){

        String email = memberDto.getEmail();
        String password = memberDto.getPassword();
        log.info("email={}, password={}", email, password);

        if (password.equals(againPassWord)){
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.PASSWORD_WRONG), HttpStatus.OK);
        }

        // 성공 로직

        Member member = new Member(email, password);
        memberServiceImpl.join(member);

        // 인증 메일 발송

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS), HttpStatus.OK);
    }

    /**
     *
     * @param email
     * @return 기존회원-> ok, 새로운 회원-> not_found
     */
//    @PostMapping("/enter-email")
//    public ResponseEntity<String> enterEmail(@RequestBody String email){
//        if (memberServiceImpl.isExistingMember(email)){
//            return new ResponseEntity<>(email, HttpStatus.OK);
//        }
//        else {
//            return new ResponseEntity(DefaultRes.res(StatusCode))
//        }
//    }
}
