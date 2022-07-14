package com.petchatbot.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_serial;
    private String user_email;
    private String user_password;

    public User() {
    }
    public User(String user_email, String user_password) {
        this.user_email = user_email;
        this.user_password = user_password;
    }

}
