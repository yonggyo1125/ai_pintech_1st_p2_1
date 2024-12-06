package org.koreait.mypage.controllers;

import lombok.Data;

@Data
public class RequestProfile {

    private String name; // 회원명
    private String nickName;
    private String password;
    private String confirmPassword;
}
