package com.example.desktime.config;

import com.example.desktime.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendUserCreationMail(String userName, String password, String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Welcome to WorkPlus!");
        // Construct the email body with user details and password
        String emailContent = "Dear " + userName + ",\n\n"
                + "Welcome to Our Application! Your account has been successfully created.\n\n"
                + "Here are your account details:\n"
                + "Email: " + userEmail + "\n"
                + "Username: " + userName + "\n"
                + "Password: " + password + "\n\n"
                + "Please keep this information secure and do not share it with anyone.\n\n"
                + "Thank you for joining us!";
        message.setText(emailContent);
        javaMailSender.send(message);
    }
}
