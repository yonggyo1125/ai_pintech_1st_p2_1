package org.koreait.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();

        // requestLogin 세션값 비우기
        session.removeAttribute("requestLogin");

        // UserDetails 구현체

        //MemberInfo memberInfo = (MemberInfo)authentication.getPrincipal();
        //System.out.println(memberInfo);

        /**
         * 로그인 성공시 페이지 이동
         * 1) redirectUrl에 지정된 주소
         * 2) redirectUrl이 없는 경우는 메인 페이지 이동
         */

        String redirectUrl = request.getParameter("redirectUrl");
        redirectUrl = StringUtils.hasText(redirectUrl) ? redirectUrl : "/";

        response.sendRedirect(request.getContextPath() + redirectUrl);

    }
}
