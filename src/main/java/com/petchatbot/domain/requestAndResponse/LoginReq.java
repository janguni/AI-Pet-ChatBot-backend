package com.petchatbot.domain.requestAndResponse;

import lombok.Data;
import lombok.Getter;

@Data
public class LoginReq {

    private String email;
    private String password;
    private String againPassword;
}
