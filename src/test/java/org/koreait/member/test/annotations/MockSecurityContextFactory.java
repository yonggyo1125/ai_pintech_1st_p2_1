package org.koreait.member.test.annotations;

import org.koreait.member.MemberInfo;
import org.koreait.member.entities.Authorities;
import org.koreait.member.entities.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MockSecurityContextFactory implements WithSecurityContextFactory<MockMember> {
    @Override
    public SecurityContext createSecurityContext(MockMember annotation) {
        Member member = new Member();
        member.setSeq(annotation.seq());
        member.setEmail(annotation.email());
        member.setPassword(annotation.password());
        member.setName(annotation.name());
        member.setNickName(annotation.nickName());
        member.setBirthDt(LocalDate.now().minusYears(20L));
        member.setRequiredTerms1(true);
        member.setRequiredTerms2(true);
        member.setRequiredTerms3(true);
        member.setCredentialChangedAt(LocalDateTime.now());

        List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.authority())
                .map(a -> new SimpleGrantedAuthority(a.name()))
                .toList();

        List<Authorities> _authorities = Arrays.stream(annotation.authority())
                .map(a -> {
                  Authorities auth = new Authorities();
                  auth.setAuthority(a);
                  auth.setMember(member);
                  return auth;
                }).toList();
        member.setAuthorities(_authorities);

        MemberInfo memberInfo = MemberInfo
                .builder()
                .email(annotation.email())
                .password(annotation.password())
                .member(member)
                .authorities(authorities)
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(memberInfo, annotation.password(), authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token); // 로그인 처리

        return context;
    }
}
