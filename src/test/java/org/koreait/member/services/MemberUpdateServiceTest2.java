package org.koreait.member.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.member.constants.Gender;
import org.koreait.mypage.controllers.RequestProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@DisplayName("회원 정보 수정 기능 테스트")
public class MemberUpdateServiceTest2 {

    @Autowired
    private MemberUpdateService service;

    @Test
    @DisplayName("회원정보 수정 성공시 예외가 발생하지 않는 테스트")
    void updateSuccessTest() {
        assertDoesNotThrow(() -> {
            RequestProfile form = new RequestProfile();
            form.setEmail("user01@test.org");
            form.setName("이이름");
            form.setNickName("닉네임");
            form.setZipCode("0000");
            form.setAddress("주소");
            form.setAddressSub("나머지 주소");
            form.setGender(Gender.MALE);
            form.setBirthDt(LocalDate.now());

            service.process(form);
        });
    }
}
