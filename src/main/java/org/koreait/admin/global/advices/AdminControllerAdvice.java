package org.koreait.admin.global.advices;

import lombok.RequiredArgsConstructor;
import org.koreait.member.entities.Member;
import org.koreait.member.libs.MemberUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice("org.koreait.admin")
public class AdminControllerAdvice {
    private final MemberUtil memberUtil;

    @ModelAttribute("profile")
    public Member profile() {
        return memberUtil.getMember();
    }
}
