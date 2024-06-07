package com.swift.evergrowfinance.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    private static final String SENDER_EMAIL_ADDRESS = "akaks9090@gmail.com";
    private static final String SENDER_EMAIL_PASSWORD = "jpiwvpbgxslqjbld";
    private static final String SENDER_HOST = "smtp.gmail.com";
    private static final String SENDER_PORT = "587";

    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = "http://localhost:3000/reset-password/" + token;
        String messageContent = "<p>To reset your password, click the link below:</p>"
                + "<a href=\"" + resetUrl + "\">" + resetUrl + "</a>";
        sendEmail(email, messageContent);
    }

    public void sendEmail(String RECEIVER_EMAIL_ADDRESS, String emailContent) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SENDER_HOST);
        properties.put("mail.smtp.port", SENDER_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL_ADDRESS, SENDER_EMAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(RECEIVER_EMAIL_ADDRESS));
            message.setSubject("Password Reset Request");
            message.setContent(emailContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email is successfully sent!");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send the email.");
        }
    }
}
