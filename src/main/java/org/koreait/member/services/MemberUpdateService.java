package org.koreait.member.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.global.exceptions.scripts.AlertException;
import org.koreait.global.libs.Utils;
import org.koreait.member.constants.Authority;
import org.koreait.member.controllers.RequestJoin;
import org.koreait.member.entities.Authorities;
import org.koreait.member.entities.Member;
import org.koreait.member.entities.QAuthorities;
import org.koreait.member.libs.MemberUtil;
import org.koreait.member.repositories.AuthoritiesRepository;
import org.koreait.member.repositories.MemberRepository;
import org.koreait.mypage.controllers.RequestProfile;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Lazy // 지연로딩 - 최초로 빈을 사용할때 생성
@Service
@RequiredArgsConstructor
@Transactional
public class MemberUpdateService {

    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MemberUtil memberUtil;
    private final MemberInfoService infoService;
    private final HttpSession session;
    private final Utils utils;

    /**
     * 커맨드 객체의 타입에 따라서 RequestJoin이면 회원 가입 처리
     *                      RequestProfile이면 회원정보 수정 처리
     * @param form
     */
    public void process(RequestJoin form) {
        // 커맨드 객체 -> 엔티티 객체 데이터 옮기기
        Member member = modelMapper.map(form, Member.class);

        // 선택 약관 -> 약관 항목1||약관 항목2||...
        List<String> optionalTerms = form.getOptionalTerms();
        if (optionalTerms != null) {
            member.setOptionalTerms(String.join("||", optionalTerms));
        }

        // 비밀번호 해시화 - BCrypt
        if (!form.isSocial()) {
            String hash = passwordEncoder.encode(form.getPassword());
            member.setPassword(hash);
            member.setCredentialChangedAt(LocalDateTime.now());
        }

        // 소셜 로그인 관련
        member.setSocialChannel(form.getSocialChannel());
        member.setSocialToken(form.getSocialToken());

        // 회원 권한
        Authorities auth = new Authorities();
        auth.setMember(member);
        auth.setAuthority(Authority.USER);  // 회원 권한이 없는 경우 - 회원 가입시, 기본 권한 USER

        save(member, List.of(auth)); // 회원 저장 처리
    }

    /**
     * 회원정보 수정
     * @param form
     */
    public void process(RequestProfile form) {
        process(form, null);
    }

    public void process(RequestProfile form, List<Authority> authorities) {
        String email = form.getEmail();
        Member member = memberUtil.isAdmin() && StringUtils.hasText(email) ? memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email)) : memberUtil.getMember(); // 로그인한 사용자의 정보

        member.setName(form.getName());
        member.setNickName(form.getNickName());
        member.setBirthDt(form.getBirthDt());
        member.setGender(form.getGender());
        member.setZipCode(form.getZipCode());
        member.setAddress(form.getAddress());
        member.setAddressSub(form.getAddressSub());

        List<String> optionalTerms = form.getOptionalTerms();
        if (optionalTerms != null) {
            member.setOptionalTerms(String.join("||", optionalTerms));
        }

        // 회원정보 수정일때는 비밀번호가 입력 된 경우만 저장
        String password = form.getPassword();
        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
            member.setCredentialChangedAt(LocalDateTime.now());
        }

        /**
         * 회원 권한은 관리자만 수정 가능!
         *
         */
        List<Authorities> _authorities = null;
        if (authorities != null && memberUtil.isAdmin()) {
            _authorities = authorities.stream().map(a -> {
               Authorities auth = new Authorities();
               auth.setAuthority(a);
               auth.setMember(member);
               return auth;
            }).toList();
        }

        save(member, _authorities);

        // 로그인 회원 정보 업데이트
        if (!StringUtils.hasText(email)) {
            Member _member = memberRepository.findByEmail(member.getEmail()).orElse(null);
            if (_member != null) {
                infoService.addInfo(_member);
                session.setAttribute("member", _member);
            }
        }
    }

    /**
     * 회원정보 추가 또는 수정 처리
     *
     */
    private void save(Member member, List<Authorities> authorities) {

        memberRepository.saveAndFlush(member);

        // 회원 권한 업데이트 처리 S

        if (authorities != null) {
            /**
             * 기존 권한을 삭제하고 다시 등록
             */

            QAuthorities qAuthorities = QAuthorities.authorities;
            List<Authorities> items = (List<Authorities>) authoritiesRepository.findAll(qAuthorities.member.eq(member));
            if (items != null) {
               authoritiesRepository.deleteAll(items);
               authoritiesRepository.flush();
            }


            authoritiesRepository.saveAllAndFlush(authorities);
        }

        // 회원 권한 업데이트 처리 E
    }

    /**
     * 회원 목록 수정 처리
     *
     * @param chks
     */
    public void updateList(List<Integer> chks) {
        if (chks == null || chks.isEmpty()) {
            throw new AlertException("수정할 회원을 선택하세요.");
        }

        List<Member> members = new ArrayList<>();
        for (int chk : chks) {
            Long seq = Long.valueOf(utils.getParam("seq_" + chk));
            Member member = memberRepository.findById(seq).orElse(null);
            if (member == null) continue;

            // 비밀번호 변경일시 업데이트
            if (utils.getParam("updateCredentialChangedAt_" + chk) != null) {
                member.setCredentialChangedAt(LocalDateTime.now());
            }

            // 탈퇴 취소 또는 탈퇴 처리
            String deletedAt = utils.getParam("deletedAt_" + chk);
            if (deletedAt != null) {
                member.setDeletedAt(deletedAt.equals("CANCEL") ? null : LocalDateTime.now());
            }

            members.add(member);
        }

        memberRepository.saveAllAndFlush(members);
    }
}
