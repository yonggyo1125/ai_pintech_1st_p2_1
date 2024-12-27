package org.koreait.admin.basic.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.admin.basic.services.TermsInfoService;
import org.koreait.admin.basic.services.TermsUpdateService;
import org.koreait.admin.global.menu.SubMenus;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.entities.SiteConfig;
import org.koreait.global.entities.Terms;
import org.koreait.global.libs.Utils;
import org.koreait.global.services.CodeValueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/admin/basic")
public class BasicController implements SubMenus {

    private final CodeValueService codeValueService;
    private final TermsUpdateService termsUpdateService;
    private final TermsInfoService termsInfoService;
    private final HttpServletRequest request;

    private final Utils utils;

    @ModelAttribute("menuCode")
    public String menuCode() {
        return "basic";
    }

    /**
     * 사이트 기본 정보 설정
     *
     * @param model
     * @return
     */
    @GetMapping({"", "/siteConfig"})
    public String siteConfig(Model model) {
        commonProcess("siteConfig", model);

        SiteConfig form = Objects.requireNonNullElseGet(codeValueService.get("siteConfig", SiteConfig.class), SiteConfig::new);

        model.addAttribute("siteConfig", form);

        return "admin/basic/siteConfig";
    }

    /**
     * 사이트 기본 정보 설정 처리
     *
     * @param form
     * @param model
     * @return
     */
    @PatchMapping("/siteConfig")
    public String siteConfigPs(SiteConfig form, Model model) {
        commonProcess("siteConfig", model);

        codeValueService.save("siteConfig", form);

        utils.showSessionMessage("저장되었습니다.");

        return "admin/basic/siteConfig";
    }

    // 약관 관리 양식, 목록
    @GetMapping("/terms")
    public String terms(@ModelAttribute Terms form, Model model) {
        commonProcess("terms", model);

        List<Terms> items = termsInfoService.getList();
        model.addAttribute("items", items);

        return "admin/basic/terms";
    }

    // 약관 등록 처리
    @PostMapping("/terms")
    public String termsPs(@Valid Terms form, Errors errors, Model model) {
        commonProcess("terms", model);

        if (errors.hasErrors()) {
            return "admin/basic/terms";
        }

        termsUpdateService.save(form);

        model.addAttribute("script", "parent.location.reload();");

        return "common/_execute_script";
    }

    @RequestMapping(path="/terms", method={RequestMethod.PATCH, RequestMethod.DELETE})
    public String updateTerms(@RequestParam(name="chk", required = false) List<Integer> chks, Model model) {

        termsUpdateService.processList(chks);


        String message = request.getMethod().equalsIgnoreCase("DELETE") ? "삭제" : "수정";
        message += "하였습니다.";
        utils.showSessionMessage(message);

        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    /**
     * 기본설정 공통 처리 부분
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {

        mode = StringUtils.hasText(mode) ? mode : "siteConfig";
        String pageTitle = null;
        if (mode.equals("siteConfig")) {
            pageTitle = "사이트 기본정보";
        } else if (mode.equals("terms")) {
            pageTitle = "약관 관리";
        }

        pageTitle += " - 기본설정";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subMenuCode", mode);
    }
}
