package com.example.workplus.service;

import jakarta.mail.MessagingException;

public interface OTPService {
    String generateOTP(String userMailId) throws MessagingException;
    boolean validateOTP(String userMailId, String otp);
}
