package org.koreait.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.global.menu.SubMenus;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.member.constants.Authority;
import org.koreait.member.entities.Member;
import org.koreait.member.services.MemberInfoService;
import org.koreait.member.services.MemberUpdateService;
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
    private final MemberInfoService memberInfoService;
    private final MemberUpdateService memberUpdateService;

    @ModelAttribute("menuCode")
    public String menuCode() {
        return "member";
    }

    @ModelAttribute("authorities")
    public Authority[] authorities() {
        return Authority.values();
    }

    /**
     * 회원목록
     * @param model
     * @return
     */
    @GetMapping({"", "/list"})
    public String list(@ModelAttribute MemberSearch search, Model model) {
        commonProcess("list", model);

        ListData<Member> data = memberInfoService.getList(search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "admin/member/list";
    }

    /**
     * 회원목록 수정 처리
     * @param model
     * @return
     */
    @PatchMapping("/list")
    public String listPs(@RequestParam(name="chk", required = false) List<Integer> chks, Model model) {

        memberUpdateService.updateList(chks);

        utils.showSessionMessage("수정하였습니다.");
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    @GetMapping("/info/{email}")
    public String info(@PathVariable("email") String email, Model model) {

        return "admin/member/info";
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
