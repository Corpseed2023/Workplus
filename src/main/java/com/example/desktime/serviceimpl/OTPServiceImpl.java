//package com.example.desktime.serviceimpl;
//
//import com.example.desktime.service.OTPService;
//import org.springframework.stereotype.Service;
//
//import java.security.SecureRandom;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class OTPServiceImpl implements OTPService {
//
//    private static final SecureRandom random = new SecureRandom();
//    private static final int OTP_LENGTH = 4;
//    private static final long OTP_EXPIRATION_MINUTES = 30;
//
//    // Thread-safe in-memory storage for OTPs
//    private final Map<Long, String> otpStore = new ConcurrentHashMap<>();
//    private final Map<Long, Long> otpTimestampStore = new ConcurrentHashMap<>();
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//    // Constructor to initialize the scheduled task
//    public OTPServiceImpl() {
//        // Schedule a task to remove expired OTPs every minute
//        scheduler.scheduleAtFixedRate(this::removeExpiredOTPs, 0, 1, TimeUnit.MINUTES);
//    }
//
//    @Override
//    public String generateOTP(Long userId) {
//        StringBuilder otp = new StringBuilder(OTP_LENGTH);
//        for (int i = 0; i < OTP_LENGTH; i++) {
//            otp.append(random.nextInt(10));
//        }
//        String otpStr = otp.toString();
//        otpStore.put(userId, otpStr); // Store the OTP in a thread-safe map
//        otpTimestampStore.put(userId, System.currentTimeMillis()); // Store the creation timestamp
//        return otpStr;
//    }
//
//    @Override
//    public boolean validateOTP(Long userId, String otp) {
//        return otpStore.containsKey(userId) && otpStore.get(userId).equals(otp);
//    }
//
//    private void removeExpiredOTPs() {
//        long currentTime = System.currentTimeMillis();
//        // Remove expired OTPs based on the creation timestamp
//        otpTimestampStore.entrySet().removeIf(entry ->
//                currentTime - entry.getValue() > TimeUnit.MINUTES.toMillis(OTP_EXPIRATION_MINUTES));
//        otpStore.keySet().removeIf(userId -> !otpTimestampStore.containsKey(userId));
//    }
//}
package com.example.desktime.serviceimpl;

import com.example.desktime.config.EmailService;
import com.example.desktime.model.OTP;
import com.example.desktime.model.User;
import com.example.desktime.repository.OTPRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.service.OTPService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 4;
    private static final long OTP_EXPIRATION_MINUTES = 30;

    private final Map<Long, String> otpCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private OTPRepository otpRepository;

//    public OTPServiceImpl() {
//        // Schedule a task to remove expired OTPs from the database every minute
//        scheduler.scheduleAtFixedRate(this::removeExpiredOTPsFromDatabase, 0, 1, TimeUnit.MINUTES);
//    }

    @Override
    public String generateOTP(String userMailId) throws MessagingException {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append((int) (Math.random() * 10));
        }
        String otpStr = otp.toString();

        User userData = userRepository.findUserByEmail(userMailId);
        if (userData == null) {
            throw new IllegalArgumentException("User not found with email: " + userMailId);
        }

        // Store OTP in database
        OTP otpEntity = new OTP();
        otpEntity.setUser(userData);
        otpEntity.setOtp(otpStr);
        otpEntity.setCreatedAt(new Date());
        otpEntity.setUsed(false); // Set to false initially
//        otpEntity.setExpiresAt(new Date(System.currentTimeMillis() + OTP_EXPIRATION_TIME)); // Set expiration time
        otpRepository.save(otpEntity);

        // Send OTP via email
        emailService.sendOTP(otpStr, userData.getUsername(), userMailId);

        // Optional: Store OTP in in-memory cache
        // otpCache.put(userData.getId(), otpStr);

        return otpStr;
    }

    @Override
    public boolean validateOTP(String userMailId, String otp) {
        User userData = userRepository.findUserByEmail(userMailId);
        if (userData == null) {
            throw new IllegalArgumentException("User not found with email: " + userMailId);
        }

        // Check in the database for the OTP
        List<OTP> otps = otpRepository.findByUserId(userData.getId());
        for (OTP otpEntity : otps) {
            if (otpEntity.getOtp().equals(otp) && otpEntity.isUsed()) {
                // Update the OTP to mark it as used
                otpEntity.setUsed(false);
                otpEntity.setUpdatedAt(new Date()); // Optionally update the timestamp
                otpRepository.save(otpEntity); // Save the updated OTP entity

                // Optionally update cache for future checks
                otpCache.put(userData.getId(), otp);
                return true;
            }
        }
        return false;
    }

    private void removeExpiredOTPsFromDatabase() {
        Date now = new Date();
        List<OTP> otps = otpRepository.findAll();
        for (OTP otpEntity : otps) {
            long age = now.getTime() - otpEntity.getCreatedAt().getTime();
            if (age > TimeUnit.MINUTES.toMillis(OTP_EXPIRATION_MINUTES)) {
                otpEntity.setUsed(false);
                otpRepository.save(otpEntity);
            }
        }
    }
}
