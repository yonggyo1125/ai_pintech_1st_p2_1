package org.koreait.member.social.services;


import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.koreait.global.services.CodeValueService;
import org.koreait.member.repositories.MemberRepository;
import org.koreait.member.social.entities.AuthToken;
import org.koreait.member.social.entities.SocialConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Lazy
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final CodeValueService codeValueService;
    private final Utils utils;

    @Override
    public String getToken(String code) {
        SocialConfig socialConfig = codeValueService.get("socialConfig", SocialConfig.class);
        String restApiKey = socialConfig.getKakaoRestApiKey();
        if (!socialConfig.isUseKakaoLogin() || !StringUtils.hasText(restApiKey)) {
            return null;
        }

        /* Access Token 발급 S */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", restApiKey);
        params.add("redirect_uri", utils.getUrl("/member/social/callback/kakao"));
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<AuthToken> response = restTemplate.postForEntity(URI.create("https://kauth.kakao.com/oauth/token"), request, AuthToken.class);

        AuthToken token = response.getBody();
        /* Access Token 발급 E */

        return "";
    }

    @Override
    public boolean login(String token) {
        return false;
    }
}
