package com.example.desktime.serviceimpl;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.model.User;
import com.example.desktime.repository.DailyActivityRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyActivityServiceImpl implements DailyActivityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    @Override
    public void saveDailyActivity(String email, LocalDate date) {
        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Create a new DailyActivity instance
            DailyActivity dailyActivity = new DailyActivity(user, date, true); // Assuming present is true by default

            // Save the daily activity
            dailyActivityRepository.save(dailyActivity);
        } else {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }
    }
}
