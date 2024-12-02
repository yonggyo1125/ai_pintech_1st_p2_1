package org.koreait.member.services;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.member.controllers.RequestJoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Locale;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class MemberUpdateServiceTest {

    @Autowired
    private MemberUpdateService updateService;

    @Test
    @DisplayName("회원 가입 기능 테스트")
    void joinTest() {
        Faker faker = new Faker(Locale.KOREA);

        RequestJoin form = new RequestJoin();
        form.setEmail(faker.internet().emailAddress());
        form.setPassword("_aA123456");
        form.setName(faker.name().fullName());
        form.setBirthDt(LocalDate.now().minusYears(20L));
        form.setZipCode(faker.address().zipCode());
        form.setAddress(faker.address().fullAddress());
        form.setAddressSub(faker.address().buildingNumber());
    }
}
