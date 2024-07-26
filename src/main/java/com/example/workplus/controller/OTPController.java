package com.example.workplus.controller;

import com.example.workplus.service.OTPService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OTPController {

    @Autowired
    private OTPService otpService;


    @GetMapping("/generate-otp")
    public String generateOTP(@RequestParam String userMailId) throws MessagingException {
        return otpService.generateOTP(userMailId);
    }

    @PostMapping("/validate-otp")
    public boolean validateOTP(@RequestParam String userMailId, @RequestParam String otp) {
        return otpService.validateOTP(userMailId, otp);
    }
}
