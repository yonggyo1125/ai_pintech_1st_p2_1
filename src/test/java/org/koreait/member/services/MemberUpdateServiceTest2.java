package org.koreait.member.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.member.constants.Gender;
import org.koreait.member.controllers.RequestJoin;
import org.koreait.member.entities.Member;
import org.koreait.member.repositories.MemberRepository;
import org.koreait.mypage.controllers.RequestProfile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@DisplayName("회원 정보 수정 기능 테스트")
public class MemberUpdateServiceTest2 {

    @Autowired
    private MemberUpdateService service;

    @Autowired
    private MemberRepository repository;

    @Autowired
    private ModelMapper mapper;

    private RequestProfile profile;

    @BeforeEach
    void init() {
        RequestJoin form = new RequestJoin();
        form.setEmail("user01@test.org");
        form.setName("이이름");
        form.setNickName("닉네임");
        form.setZipCode("0000");
        form.setAddress("주소");
        form.setAddressSub("나머지 주소");
        form.setGender(Gender.MALE);
        form.setBirthDt(LocalDate.now());
        form.setPassword("_aA123456");
        form.setConfirmPassword(form.getPassword());

        service.process(form);

        Member member = repository.findByEmail(form.getEmail()).orElse(null);
        profile = mapper.map(member, RequestProfile.class);
    }

    @Test
    @DisplayName("회원정보 수정 성공시 예외가 발생하지 않는 테스트")
    @WithUserDetails(value="user01@test.org", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateSuccessTest() {
        profile.setName("(수정)이이름");
        assertDoesNotThrow(() -> {
            service.process(profile);
        });

        Member member = repository.findByEmail(profile.getEmail()).orElse(null);
        assertEquals(profile.getName(), member.getName());
    }
}
