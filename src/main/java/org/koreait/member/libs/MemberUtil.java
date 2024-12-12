package org.koreait.member.libs;

import lombok.RequiredArgsConstructor;
import org.koreait.member.MemberInfo;
import org.koreait.member.constants.Authority;
import org.koreait.member.entities.Member;
import org.koreait.member.services.MemberInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Lazy
@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberInfoService infoService;

    public boolean isLogin() {
        return getMember() != null;
    }

    /**
     * 관리자 여부
     *  권한 - MANAGER, ADMIN
     * @return
     */
    public boolean isAdmin() {
         return isLogin() &&
                    getMember().getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority() == Authority.ADMIN || a.getAuthority() == Authority.MANAGER);
    }

    /**
     * 로그인 한 회원의 정보 조회
     *
     * @return
     */
    public Member getMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof MemberInfo _memberInfo) {
            MemberInfo memberInfo = (MemberInfo)infoService.loadUserByUsername(_memberInfo.getEmail());
            return memberInfo.getMember();
        }

        return null;
    }
}
