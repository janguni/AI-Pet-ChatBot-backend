package com.petchatbot.controller;

import com.petchatbot.config.ResponseMessage;
import com.petchatbot.config.StatusCode;
import com.petchatbot.domain.requestAndResponse.LoginReq;
import com.petchatbot.domain.dto.MemberDto;
import com.petchatbot.domain.requestAndResponse.DefaultRes;
import com.petchatbot.repository.MemberRepository;
import com.petchatbot.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberServiceImpl memberServiceImpl;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody LoginReq loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String againPassword = loginRequest.getAgainPassword();

        if (!memberServiceImpl.isEqualPassword(password, againPassword)){
            return new ResponseEntity(
                    DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.PASSWORD_WRONG),
                    HttpStatus.OK);
        }

        // 성공 로직
        MemberDto memberDto = new MemberDto(email, password);
        log.info("email={}, password={}", email, password);
        memberServiceImpl.join(memberDto);

        // 인증 메일 발송


        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SEND_EMAIL), HttpStatus.OK);
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
