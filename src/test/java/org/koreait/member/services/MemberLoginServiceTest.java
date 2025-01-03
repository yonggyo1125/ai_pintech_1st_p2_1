package org.koreait.member.services;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class MemberLoginServiceTest {

    private MemberLoginService service;
    private HttpServletRequest request;

    @BeforeEach
    void init() {
        request = mock(HttpServletRequest.class); // 모의 객체

        service = new MemberLoginService(request);
    }

    @Test
    void test1() {
        service.process();
    }
}
