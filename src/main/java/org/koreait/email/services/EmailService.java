package org.koreait.email.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.koreait.email.controllers.RequestEmail;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Profile("email")
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

            List<String> to = form.getTo();
            List<String> cc = form.getCc();
            List<String> bcc = form.getBcc();
            String subject = form.getSubject();
            String content = form.getContent();

            tplData.put("to", to);
            tplData.put("cc", cc);
            tplData.put("bcc", bcc);
            tplData.put("subject", subject);
            tplData.put("content", content);

            context.setVariables(tplData);

            String html = templateEngine.process("email/" + tpl, context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(form.getTo().toArray(String[]::new));

            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(String[]::new));
            }

            if (bcc != null && !bcc.isEmpty()) {
                helper.setBcc(bcc.toArray(String[]::new));
            }

            helper.setSubject(subject);
            helper.setText(html, true);

            javaMailSender.send(message);

            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean sendEmail(RequestEmail form, String tpl) {
        return sendEmail(form, tpl, null);
    }

    public boolean sendEmail(String to, String subject, String content) {
        RequestEmail form = new RequestEmail();
        form.setTo(List.of(to));
        form.setSubject(subject);
        form.setContent(content);

        return sendEmail(form, "general");
    }
}
