package org.koreait.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final Utils utils;

    @GetMapping("/login")
    public String login(@ModelAttribute RequestLogin form) {

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

    /**
     * 회원가입 약관 동의
     *
     * @return
     */
    @GetMapping("/agree")
    public String joinAgree(@ModelAttribute RequestAgree form) {

        return utils.tpl("member/agree");
    }

    /**
     * 회원 가입 양식 페이지
     * - 필수 약관 동의 여부 검증
     *
     * @return
     */
    @PostMapping("/join")
    public String join(@Valid RequestAgree form, Errors errors) {

        log.info(form.toString());

        if (errors.hasErrors()) { // 약관 동의를 하지 않았다면 약관 동의 화면을 출력
            return utils.tpl("member/agree");
        }

        return utils.tpl("member/join");
    }

    /***
     * 회원가입 처리
     *
     * @return
     */
    @PostMapping("/join_ps")
    public String joinPs(@Valid RequestJoin form, Errors errors) {

        if (errors.hasErrors()) {
            return utils.tpl("member/join");
        }

        // 회원가입 처리 완료 후 - 로그인 페이지로 이동
        return "redirect:/member/login";
    }
}
