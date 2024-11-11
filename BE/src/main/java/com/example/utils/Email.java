package com.example.utils;

import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

@Service
public class Email {
    //thanhkvph34331@fpt.edu.vn
    //oisr ezhc zrgh oeaq
    private final String fromEmail = "nguyenchuc812@gmail.com";
    private final String password = "dkmr nqzc lfuz htje";
    private final String defaultToEmail = "chienngoclong2k4@gmail.com";

    public void sendEmail(String toEmail, String subject, String body) {

        if (toEmail == null || toEmail.isEmpty()) {
            toEmail = defaultToEmail;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("Email : "+ fromEmail + "--" +  "Pass : " + password);
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
        } catch (MessagingException e) {
            System.out.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }

}

