package com.petchatbot.domain.DTO;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserDto {

    @Id
    @GeneratedValue
    private Long serial;
    @Column(nullable = false, unique = true, length=50)
    private String email;
    @Column(nullable = false, unique = true, length=50)
    private String password;

    public UserDto() {
    }

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
