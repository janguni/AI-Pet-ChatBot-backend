package com.petchatbot.config.auth.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.petchatbot.config.ResponseMessage;
import com.petchatbot.config.StatusCode;
import com.petchatbot.config.auth.PrincipalDetails;
import com.petchatbot.domain.model.Member;
import com.petchatbot.domain.requestAndResponse.DefaultRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import com.auth0.jwt.JWT;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // 로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JWTAuthenticationFilter: 로그인 시도중");

        try {

            ObjectMapper om = new ObjectMapper();
            Member member = om.readValue(request.getInputStream(), Member.class);
            log.info("memberEmail={}, memberPassword={}", member.getMemberEmail(), member.getMemberPassword());

            // 토큰 생성 (인증용 객체)
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getMemberEmail(), member.getMemberPassword());


            // 정상적인 로그인 시도인지 검증
            // 정상이면 authentication 객체를 반환
            Authentication authentication = authenticationManager.authenticate(authenticationToken);


            // (확인용임) 로그인 시도를 한 member의 이메일 확인
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("확인용 ={}",principalDetails.getMember().getMemberEmail());

            return authentication;

        } catch (IOException e) {
            log.error("login 실패");
            throw new RuntimeException();
        }
        //return null;
    }

    // attemptAuthentication 함수가 정상적으로 동작하면 호출되는 함수
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication 실행됨: 인증이 완료되었다는 뜻");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // Hash암호방식
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                .withClaim("id", principalDetails.getMember().getMember_serial())
                .withClaim("username", principalDetails.getMember().getMemberEmail())
                .sign(Algorithm.HMAC512("cos")); // secret key

        response.addHeader("Authorization", "Bearer "+jwtToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("unsuccessfulAuthentication 실행됨: 인증에 실패했다는 뜻");
        setFailResponse(request, response);
    }

    private void setFailResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("statusCode", StatusCode.UNAUTHORIZED);
        jsonObject.addProperty("responseMessage", ResponseMessage.LOGIN_FAIL);
        response.getWriter().print(jsonObject);

    }
}
