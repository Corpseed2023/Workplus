package com.example.desktime.serviceimpl;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.model.User;
import com.example.desktime.repository.DailyActivityRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class DailyActivityServiceImpl implements DailyActivityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DailyActivityRepository dailyActivityRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public DailyActivityResponse saveDailyActivity(DailyActivityRequest request) {
        try {
            LocalDate activityDate = LocalDate.parse(request.getDate(), dateFormatter);
            LocalDateTime loginTime = LocalDateTime.parse(request.getLoginTime());

            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                DailyActivity dailyActivity = new DailyActivity(user, activityDate, loginTime, null, true, null);
                dailyActivity.setDayOfWeek(activityDate.getDayOfWeek().toString());
                DailyActivity savedActivity = dailyActivityRepository.save(dailyActivity);

                DailyActivityResponse response = new DailyActivityResponse();
                response.setId(savedActivity.getId());
                response.setUserEmail(request.getEmail());
                response.setDate(request.getDate());
                response.setLoginTime(request.getLoginTime());
                response.setPresent(true);
                response.setDayOfWeek(savedActivity.getDayOfWeek());
                response.setAttendanceType(savedActivity.getAttendanceType().toString());
                return response;
            } else {
                throw new IllegalArgumentException("User with email " + request.getEmail() + " not found");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please provide the date in yyyy-MM-dd format.");
        }
    }

}
