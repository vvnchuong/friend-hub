package com.friendhub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String email, String link) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Reset your password");
        mail.setText("""
                You requested to reset your password.

                Click the link below:
                %s

                This link will expire in 15 minutes.
                If you did not request this, please ignore this email.
                """.formatted(link));

        mailSender.send(mail);
    }
}
