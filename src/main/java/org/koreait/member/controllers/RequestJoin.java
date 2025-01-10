package org.koreait.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.koreait.member.constants.Gender;
import org.koreait.member.social.constants.SocialChannel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Data
public class RequestJoin extends RequestAgree {

    @Email
    @NotBlank
    private String email; // 이메일

    @NotBlank
    private String name; // 회원명

    @Size(min=8)
    private String password; // 비밀번호

    private String confirmPassword; // 비밀번호 확인

    @NotBlank
    private String nickName; // 닉네임

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDt;  // 생년월일

    @NotNull
    private Gender gender; // 성별

    @NotBlank
    private String zipCode; // 우편번호

    @NotBlank
    private String address; // 주소
    private String addressSub; // 나머지 주소

    private SocialChannel socialChannel;
    private String socialToken;

    // 소셜 로그인으로 가입하는 건지 체크
    public boolean isSocial() {
        return socialChannel != null && socialChannel != SocialChannel.NONE && StringUtils.hasText(socialToken);
    }
}
