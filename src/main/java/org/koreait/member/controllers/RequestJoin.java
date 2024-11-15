package org.koreait.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.koreait.member.constants.Gender;

import java.time.LocalDate;

@Data
public class RequestJoin extends RequestAgree {

    @Email
    @NotBlank
    private String email; // 이메일

    @NotBlank
    private String name; // 회원명

    @NotBlank
    private String password; // 비밀번호

    @NotBlank
    private String confirmPassword; // 비밀번호 확인

    @NotBlank
    private String nickName; // 닉네임

    @NotNull
    private LocalDate birthDt;  // 생년월일

    @NotNull
    private Gender gender; // 성별

    @NotBlank
    private String zipCode; // 우편번호

    @NotBlank
    private String address; // 주소
    private String addressSub; // 나머지 주소
}
