package org.koreait.member.social.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.member.social.services.KakaoLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/member/social")
@RequiredArgsConstructor
public class SocialController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/callback/kakao")
    public void callback(@RequestParam(name="code", required = false) String code) {

        kakaoLoginService.getToken(code);

    }
}
