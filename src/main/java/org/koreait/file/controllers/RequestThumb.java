package org.koreait.file.controllers;

import lombok.Data;

@Data
public class RequestThumb {
    private Long seq;
    private String url; // 원격 이미지 URL
    private int width;
    private int height;
}
