package org.koreait.member.social.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.koreait.global.services.CodeValueService;
import org.koreait.member.MemberInfo;
import org.koreait.member.entities.Member;
import org.koreait.member.repositories.MemberRepository;
import org.koreait.member.services.MemberInfoService;
import org.koreait.member.social.constants.SocialChannel;
import org.koreait.member.social.entities.AuthToken;
import org.koreait.member.social.entities.SocialConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Lazy
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final MemberInfoService memberInfoService;
    private final CodeValueService codeValueService;
    private final ObjectMapper om;
    private final Utils utils;
    private final HttpSession session;

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
            try {
                Map<String, String> data = om.readValue(response2.getBody(), new TypeReference<>() {});

                return data.get("id");

            } catch (JsonProcessingException e) {}
        }

        /* 회원 ID - SocialToken E */

        return null;
    }

    @Override
    public boolean login(String token) {
        Member member = memberRepository.findBySocialChannelAndSocialToken(SocialChannel.KAKAO, token);
        if (member == null) {
            return false;
        }

        MemberInfo memberInfo = (MemberInfo)memberInfoService.loadUserByUsername(member.getEmail());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberInfo, null, memberInfo.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication); // 로그인 처리

        session.setAttribute("member", memberInfo.getMember());
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        return true;
    }

    @Override
    public String getLoginUrl() {
        SocialConfig socialConfig = codeValueService.get("socialConfig", SocialConfig.class);
        String restApiKey = socialConfig.getKakaoRestApiKey();
        if (!socialConfig.isUseKakaoLogin() || !StringUtils.hasText(restApiKey)) {
            return null;
        }

        String redirectUri = utils.getUrl("/member/social/callback/kakao");

        return String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", restApiKey, redirectUri);
    }
}
