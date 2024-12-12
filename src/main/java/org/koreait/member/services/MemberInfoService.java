package org.koreait.member.services;

import lombok.RequiredArgsConstructor;
import org.koreait.file.entities.FileInfo;
import org.koreait.file.services.FileInfoService;
import org.koreait.member.MemberInfo;
import org.koreait.member.constants.Authority;
import org.koreait.member.entities.Authorities;
import org.koreait.member.entities.Member;
import org.koreait.member.repositories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final FileInfoService fileInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));


        List<Authorities> items = member.getAuthorities();
        if (items == null) {
            Authorities auth = new Authorities();
            auth.setMember(member);
            auth.setAuthority(Authority.USER);
            items = List.of(auth);
        }


        List<SimpleGrantedAuthority> authorities = items.stream().map(a -> new SimpleGrantedAuthority(a.getAuthority().name())).toList();

        // 추가 정보 처리
        addInfo(member);

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }

    /**
     * 추가 정보 처리
     * @param member
     */
    public void addInfo(Member member) {
        List<FileInfo> files = fileInfoService.getList(member.getEmail(), "profile");
        if (files != null && !files.isEmpty()) {
            member.setProfileImage(files.get(0));
        }
    }
}
