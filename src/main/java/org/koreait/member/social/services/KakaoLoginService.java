package org.koreait.member.social.services;


import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.koreait.global.services.CodeValueService;
import org.koreait.member.repositories.MemberRepository;
import org.koreait.member.social.entities.AuthToken;
import org.koreait.member.social.entities.SocialConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
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
        if (response.getStatusCode() != HttpStatus.OK) { // 정상 응답 X -> 종료
            return null;
        }

        AuthToken token = response.getBody();
        String accessToken = token.getAccessToken();
        /* Access Token 발급 E */


        /* 회원 ID - SocialToken S */
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setBearerAuth(accessToken);
        HttpEntity<Void> request2 = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = restTemplate.exchange(URI.create(url), HttpMethod.GET, request2, String.class);
        if (response2.getStatusCode() == HttpStatus.OK) {

        }

        /* 회원 ID - SocialToken E */

        return null;
    }

    @Override
    public boolean login(String token) {
        return false;
    }
}