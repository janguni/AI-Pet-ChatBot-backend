package com.petchatbot.domain.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_serial;
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String memberEmail;
    private String memberPassword;

    public Member() {
    }

    public Member(String memberEmail, String memberPassword) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
    }

    public void changePassword(String password){
        this.memberPassword = password;
    }


}
