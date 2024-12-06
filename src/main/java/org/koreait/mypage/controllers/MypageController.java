package org.koreait.mypage.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.koreait.member.entities.Member;
import org.koreait.member.libs.MemberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final Utils utils;
    private final MemberUtil memberUtil;

    @ModelAttribute("profile")
    public Member getMember() {
        return memberUtil.getMember();
    }

    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("mypage/style");
    }

    @GetMapping
    public String index(Model model) {
        commonProcess("main", model);

        return utils.tpl("mypage/index");
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        commonProcess("profile", model);

        return utils.tpl("mypage/profile");
    }

    @PatchMapping("/profile")
    public String updateProfile() {

        return null;
    }

    /**
     * 컨트롤러 공통 처리 영역
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {

    }
}
