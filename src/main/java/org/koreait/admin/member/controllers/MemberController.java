package org.koreait.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.global.menu.SubMenus;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/admin/member")
@Controller("adminMemberController")
public class MemberController implements SubMenus {

    private final Utils utils;

    @ModelAttribute("menuCode")
    public String menuCode() {
        return "member";
    }

    /**
     * 회원목록
     * @param model
     * @return
     */
    @GetMapping({"", "/list"})
    public String list(@ModelAttribute MemberSearch search, Model model) {
        commonProcess("list", model);

        return "admin/member/list";
    }

    /**
     * 회원목록 수정 처리
     * @param model
     * @return
     */
    @PatchMapping("/list")
    public String listPs(@RequestParam(name="chk", required = false) List<Integer> chks, Model model) {


        utils.showSessionMessage("수정하였습니다.");
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    /**
     * 공통 처리 부분
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";
        String pageTitle = "";
        if (mode.equals("list")) {
            pageTitle = "회원목록";
        }
        
        pageTitle += " - 회원관리";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subMenuCode", mode);
    }
}
