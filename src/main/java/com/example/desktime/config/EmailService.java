package com.example.desktime.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendUserCreationMail(String userName, String password, String userEmail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        try {
            helper.setTo(userEmail);
            helper.setSubject("Welcome to WorkPlus!");

            // Construct the HTML email body
            String htmlContent = "<html><body>"
                    + "<h2 style=\"color: #007bff;\">Welcome to WorkPlus!</h2>"
                    + "<p>Dear " + userName + ",</p>"
                    + "<p>Welcome to Our Application! Your account has been successfully created.</p>"
                    + "<p>Here are your account details:</p>"
                    + "<ul>"
                    + "<li><strong>Email:</strong> " + userEmail + "</li>"
                    + "<li><strong>Username:</strong> " + userName + "</li>"
                    + "<li><strong>Password:</strong> " + password + "</li>"
                    + "</ul>"
                    + "<p>Please keep this information secure and do not share it with anyone.</p>"
                    + "<p>Thank you for joining us!</p>"
                    + "<p>Login to your account <a href=\"https://record.corpseed.com/\">here</a>.</p>"
                    + "</body></html>";

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    public void sendPasswordResetEmail(String userEmail, String newPassword) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        try {
            helper.setTo(userEmail);
            helper.setSubject("Password Reset");

            // Construct the HTML email body
            String htmlContent = "<html><body>"
                    + "<h2 style=\"color: #007bff;\">Password Reset</h2>"
                    + "<p>Your password has been successfully reset.</p>"
                    + "<p>Here is your new password:</p>"
                    + "<p style=\"font-size: 1.2em;\"><strong>" + newPassword + "</strong></p>"
                    + "<p>Please keep this information secure and do not share it with anyone.</p>"
                    + "<p>Thank you!</p>"
                    + "<p>Login to your account <a href=\"https://record.corpseed.com/\">here</a>.</p>"
                    + "</body></html>";

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
