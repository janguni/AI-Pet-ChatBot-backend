package com.petchatbot.domain.DTO;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MemberDto {

    @Id
    @GeneratedValue
    private Long serial;
    @Column(nullable = false, unique = true, length=50)
    private String email;
    @Column(nullable = false, unique = true, length=50)
    private String password;

    public MemberDto() {
    }

    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
