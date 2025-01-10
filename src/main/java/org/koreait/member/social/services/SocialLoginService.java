package org.koreait.member.social.services;

public interface SocialLoginService {
    String getToken(String code);
    boolean login(String token);
    String getLoginUrl(String redirectUrl);
    void connect(String token);
    void disconnect();
    boolean exists(String token);
}
