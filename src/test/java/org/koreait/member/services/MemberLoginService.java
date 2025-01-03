package org.koreait.member.services;

import jakarta.servlet.http.HttpServletRequest;

public class MemberLoginService {

    private HttpServletRequest request;

    public MemberLoginService(HttpServletRequest request) {
        this.request = request;
    }

    public void process() {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.printf("%s, %s%n", email, password);

        request.setAttribute("email", email);
        request.setAttribute("password", password);
    }


}
