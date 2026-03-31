package com.trademind.notification.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String fromAddress;

    @Override
    public void send(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(fromAddress);

            mailSender.send(message);

            log.info("Email sent to {}", to);

        } catch (Exception ex) {
            log.error("Failed to send email to {}", to, ex);
            throw ex; // let service handle retry
        }
    }
}