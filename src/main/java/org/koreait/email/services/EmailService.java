package org.koreait.email.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.koreait.email.controllers.RequestEmail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     *
     * @param form
     * @param tpl : 템플릿 코드  email/{tpl}.html
     * @param tplData : 템플릿에 전달하는 데이터(EL 속성으로 추가)
     * @return
     */
    public boolean sendEmail(RequestEmail form, String tpl, Map<String, Object> tplData) {

        try {
            Context context = new Context();
            tplData = Objects.requireNonNullElseGet(tplData, HashMap::new);

            tplData.put("to", form.getTo());
            tplData.put("cc", form.getCc());
            tplData.put("bcc", form.getBcc());
            tplData.put("subject", form.getSubject());
            tplData.put("content", form.getContent());

            context.setVariables(tplData);

            String content = templateEngine.process("email/" + tpl, context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
