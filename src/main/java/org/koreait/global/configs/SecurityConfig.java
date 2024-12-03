package org.koreait.global.configs;

import org.koreait.member.services.LoginFailureHandler;
import org.koreait.member.services.LoginSuccessHandler;
import org.koreait.member.services.MemberAccessDeniedHandler;
import org.koreait.member.services.MemberAuthenticationExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 스프링 시큐리티 설정
 *
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /* 인증 설정 S - 로그인, 로그아웃 */
        http.formLogin(c -> {
           c.loginPage("/member/login") // 로그인 양식을 처리할 주소
                   .usernameParameter("email")
                   .passwordParameter("password")
                   .failureHandler(new LoginFailureHandler())
                   .successHandler(new LoginSuccessHandler());
        });

        http.logout(c -> {
           c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                   .logoutSuccessUrl("/member/login");
        });
        /* 인증 설정 E */

        /* 인가 설정 S - 페이지 접근 통제 */
        /**
         * authenticated() : 인증받은 사용자만 접근
         * anonymous() : 인증 받지 않은 사용자만 접근
         * permitAll() : 모든 사용자가 접근 가능
         * hasAuthority("권한 명칭") : 하나의 권한을 체크
         * hasAnyAuthority("권한1", "권한2", ...) : 나열된 권한 중 하나라도 충족하면 접근 가능
         * ROLE
         * ROLE_명칭
         * hasRole("명칭")
         * hasAnyRole(...)
         */
        http.authorizeHttpRequests(c -> {
            c.requestMatchers("/mypage/**").authenticated() // 인증한 회원 
                    .requestMatchers("/member/login", "/member/join", "/member/agree").anonymous() // 미인증 회원
                    .requestMatchers("/admin/**").hasAnyAuthority("MANAGER", "ADMIN") // 관리자 페이지는 MANAGER, ADMIN 권한
                    .anyRequest().permitAll(); // 나머지 페이지는 모두 접근 가능
        });

        http.exceptionHandling(c -> {
            c.authenticationEntryPoint(new MemberAuthenticationExceptionHandler())  // 미로그인시 인가 실패
                    .accessDeniedHandler(new MemberAccessDeniedHandler()); // 로그인 이후 인가 실패

        });

        /* 인가 설정 E */


        return http.build(); // 설정 무력화
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
