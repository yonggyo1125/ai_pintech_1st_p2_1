package org.koreait.member.services;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class MemberLoginServiceTest {

    private MemberLoginService service;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void init() {
        //request = mock(HttpServletRequest.class); // 모의 객체
        // 스텁
        given(request.getParameter("email")).willReturn("user01@test.org");
        given(request.getParameter("password")).willReturn("12345678");
        //given(request.getParameter("email")).willReturn(matches("[a-zA-Z0-9]{4,20}"));
        //given(request.getParameter("password")).willReturn(matches("\\w{8,16}"));
        service = new MemberLoginService(request);
    }

    @Test
    void test1() {
        service.process();

        then(request).should(atLeast(1)).getParameter(any());
    }

    @Test
    void test2() {
        service.process();

        ArgumentCaptor<String> captor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> captor2 = ArgumentCaptor.forClass(String.class);

        then(request).should(times(2)).setAttribute(captor1.capture(), captor2.capture());

        List<String> key = captor1.getAllValues();//captor1.getValue();
        List<String> value = captor2.getAllValues();
        System.out.printf("%s, %s%n", key, value);
    }
}
