package org.koreait.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.global.menu.SubMenus;
import org.koreait.global.annotations.ApplyErrorPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/admin/member")
@Controller("adminMemberController")
public class MemberController implements SubMenus {

    @ModelAttribute("menuCode")
    public String menuCode() {
        return "member";
    }


}
