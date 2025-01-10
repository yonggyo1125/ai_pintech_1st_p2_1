package org.koreait.member.social.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.member.social.entities.AuthToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@RestController
@RequestMapping("/member/social")
@RequiredArgsConstructor
public class SocialController {

    private final RestTemplate restTemplate;

    @GetMapping("/callback")
    public void callback(@RequestParam(name="code", required = false) String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "3e3f4413dec9128ec4147b3a26930ef1");
        params.add("redirect_uri", "http://localhost:3000/member/social/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<AuthToken> response = restTemplate.postForEntity(URI.create("https://kauth.kakao.com/oauth/token"), request, AuthToken.class);

        AuthToken token = response.getBody();
        System.out.println(token);
    }
}
