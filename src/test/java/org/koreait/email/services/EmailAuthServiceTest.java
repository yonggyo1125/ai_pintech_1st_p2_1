package org.koreait.email.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"default", "test", "email"})
public class EmailAuthServiceTest {

    @Autowired
    private EmailAuthService service;

    @Test
    void test1() {
        boolean result = service.sendCode("yonggyo00@kakao.com");
        System.out.println(result);
    }
}
