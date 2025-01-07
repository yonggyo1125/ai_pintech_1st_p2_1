package org.koreait.global.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.koreait.global.entities.SiteConfig;
import org.koreait.global.services.CodeValueService;
import org.koreait.member.libs.MemberUtil;
import org.koreait.message.services.MessageInfoService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

    private final CodeValueService codeValueService;
    private final MemberUtil memberUtil;
    private final MessageInfoService messageInfoService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        setSiteConfig(modelAndView);
        setProfile(modelAndView);
    }


    /* 사이트 설정 */
    private void setSiteConfig(ModelAndView mv) {
        if (mv == null) {
            return;
        }

        SiteConfig config = Objects.requireNonNullElseGet(codeValueService.get("siteConfig", SiteConfig.class), SiteConfig::new);

        mv.addObject("siteConfig", config);
    }

    /* 회원 프로필 설정 */
    private void setProfile(ModelAndView mv) {
        if (mv == null || !memberUtil.isLogin()) {
            return;
        }

        mv.addObject("profile", memberUtil.getMember());
        mv.addObject("totalUnRead", messageInfoService.totalUnRead()); // 미열람 쪽지 갯수
    }
}
