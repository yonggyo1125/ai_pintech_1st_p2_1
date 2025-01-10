package org.koreait.member.social.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.exceptions.scripts.AlertBackException;
import org.koreait.global.libs.Utils;
import org.koreait.member.social.constants.SocialChannel;
import org.koreait.member.social.services.KakaoLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ApplyErrorPage
@RequestMapping("/member/social")
@RequiredArgsConstructor
public class SocialController {

    private final KakaoLoginService kakaoLoginService;
    private final HttpSession session;
    private final Utils utils;

    @GetMapping("/callback/kakao")
    public String callback(@RequestParam(name="code", required = false) String code) {

        String token = kakaoLoginService.getToken(code);
        if (!StringUtils.hasText(token)) {
            throw new AlertBackException(utils.getMessage("UnAuthorized"), HttpStatus.UNAUTHORIZED);
        }

        boolean result = kakaoLoginService.login(token);
        if (result) { // 로그인 성공
            return "redirect:/";
        }

        // 소셜 회원 미가입 -> 회원가입 페이지 이동
        session.setAttribute("socialChannel", SocialChannel.KAKAO);
        session.setAttribute("socialToken", token);

        return "redirect:/member/agree";
    }
}
