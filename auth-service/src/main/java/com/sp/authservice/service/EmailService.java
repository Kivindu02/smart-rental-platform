package com.sp.authservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.url}")
    private String appUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String token) {
        try {
            MimeMessage message =mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Verify your email");
            helper.setText(
                    "<h3>Welcome to Smart Rental!</h3>" +
                    "<p>Click the link below to verify your email:</p>" +
                    "<a href='" + appUrl + "/auth/verify?token=" + token + "'>Verify Email</a>" +
                    "<p>This link expires in 24 hours.</p>",
                    true
            );

            mailSender.send(message);

        }catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
