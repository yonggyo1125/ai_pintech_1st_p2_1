package org.koreait.email.controllers;

import lombok.Data;

import java.util.List;

@Data
public class RequestEmail {
    private List<String> to; // 받는쪽 이메일
    private List<String> cc; // 참조
    private List<String> bcc; // 숨은참조
    private String subject; // 메일 제목
    private String content; // 메일 내용
}
