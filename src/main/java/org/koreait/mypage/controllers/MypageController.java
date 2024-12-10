package org.koreait.mypage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.koreait.member.entities.Member;
import org.koreait.member.libs.MemberUtil;
import org.koreait.member.services.MemberUpdateService;
import org.koreait.mypage.validators.ProfileValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@ApplyErrorPage
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final Utils utils;
    private final MemberUtil memberUtil;
    private final ModelMapper modelMapper;
    private final MemberUpdateService updateService;
    private final ProfileValidator profileValidator;

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

        Member member = memberUtil.getMember();
        RequestProfile form = modelMapper.map(member, RequestProfile.class);
        String optionalTerms = member.getOptionalTerms();
        if (StringUtils.hasText(optionalTerms)) {
            form.setOptionalTerms(Arrays.stream(optionalTerms.split("\\|\\|")).toList());
        }

        model.addAttribute("requestProfile", form);

        return utils.tpl("mypage/profile");
    }

    @PatchMapping("/profile")
    public String updateProfile(@Valid RequestProfile form, Errors errors, Model model) {
        commonProcess("profile", model);

        profileValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return utils.tpl("mypage/profile");
        }

        updateService.process(form);

        return "redirect:/mypage"; // 회원 정보 수정 완료 후 마이페이지 메인 이동
    }

    /**
     * 컨트롤러 공통 처리 영역
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "main";
        String pageTitle = utils.getMessage("마이페이지");

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if (mode.equals("profile")) { // 회원정보 수정
            addCommonScript.add("fileManager");
            addScript.add("mypage/profile");
            pageTitle = utils.getMessage("회원정보_수정");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageTitle", pageTitle);
    }
}
