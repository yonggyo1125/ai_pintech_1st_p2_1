package org.koreait.global.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                   .failureForwardUrl("/member/login?error=1")
                   .successForwardUrl("/");
        });

        http.logout(c -> {
           c.logoutSuccessUrl("/member/login");
        });
        /* 인증 설정 E */

        return http.build(); // 설정 무력화
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
