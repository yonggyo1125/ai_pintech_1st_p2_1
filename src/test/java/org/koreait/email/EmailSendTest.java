package org.koreait.email;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@SpringBootTest
@ActiveProfiles({"default", "test", "email"})
public class EmailSendTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

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
}
