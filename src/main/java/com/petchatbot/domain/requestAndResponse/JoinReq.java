package com.petchatbot.domain.requestAndResponse;

import lombok.Data;
import lombok.Getter;

@Data
public class JoinReq {
    private String email;
    private String password;
}
