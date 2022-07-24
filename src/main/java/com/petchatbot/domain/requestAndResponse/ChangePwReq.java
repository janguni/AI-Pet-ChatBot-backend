package com.petchatbot.domain.requestAndResponse;

import lombok.Data;

@Data
public class ChangePwReq {
    private String email;
    private String originPassword;
    private String newPassword;
}
