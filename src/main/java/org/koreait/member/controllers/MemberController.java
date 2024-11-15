package org.koreait.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final Utils utils;

    @GetMapping("/login")
    public String login() {

        return utils.tpl("member/login");
    }

    @PostMapping("/login")
    public String loginPs(@Valid RequestLogin form, Errors errors) {

        if (errors.hasErrors()) {
            return utils.tpl("member/login");
        }

        // 로그인 처리

        /**
         * 로그인 완료 후 페이지 이동
         * 1) redirectUrl 값이 전달된 경우는 해당 경로로 이동
         * 2) 없는 경우는 메인 페이지로 이동
         *
         */
        String redirectUrl = form.getRedirectUrl();
        redirectUrl = StringUtils.hasText(redirectUrl) ? redirectUrl : "/";

        return "redirect:" + redirectUrl;
    }
}
