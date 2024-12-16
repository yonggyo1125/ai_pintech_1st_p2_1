package org.koreait.email;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.koreait.email.controllers.RequestEmail;
import org.koreait.email.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles({"default", "test", "email"})
public class EmailSendTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private EmailService service;

    @Test
    void test1() throws Exception {
        /**
         * to : 받는 이메일
         * cc : 참조
         * bcc : 숨은 참조
         */
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setTo("yonggyo00@kakao.com");
        helper.setSubject("테스트 이메일 제목...");
        helper.setText("테스트 이메일 내용...");
        javaMailSender.send(message);
    }

    @Test
    void test2() {
        Context context = new Context();
        context.setVariable("subject", "테스트 제목...");

        String text = templateEngine.process("email/auth", context);

        System.out.println(text);
    }

    @Test
    void test3() {
        RequestEmail form = new RequestEmail();
        form.setTo(List.of("yonggyo00@kakao.com", "yonggyo00@kakao.com"));
        form.setCc(List.of("yonggyo1981@gmail.com"));
        form.setBcc(List.of("yonggyo1981@gmail.com"));
        form.setSubject("테스트 이메일 제목...");
        form.setContent("<h1>테스트 이메일 내용...</h1>");

        Map<String, Object> tplData = new HashMap<>();
        tplData.put("key1", "값1");
        tplData.put("key2", "값2");

        boolean result = service.sendEmail(form, "auth", tplData);
        System.out.println(result);
    }
}
