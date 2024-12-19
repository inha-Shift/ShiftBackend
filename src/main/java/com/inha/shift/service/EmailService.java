package com.inha.shift.service;

import com.inha.shift.domain.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(EmailMessage email, String code) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(setContext(code, email.getMessage()), true);
            mailSender.send(message);
            log.info("Success");
        } catch (MessagingException e) {
            log.error("{}{}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);
            switch (index){
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    public String setContext(String code, String page) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(page, context);
    }
}
