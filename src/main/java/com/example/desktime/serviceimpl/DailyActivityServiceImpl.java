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
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
            // Convert UTC time to IST
            LocalDateTime loginTimeUTC = request.getLoginTime();
            ZoneId utcZone = ZoneId.of("UTC");
            ZoneId istZone = ZoneId.of("Asia/Kolkata");
            ZonedDateTime utcDateTime = ZonedDateTime.of(loginTimeUTC, utcZone);
            ZonedDateTime istDateTime = utcDateTime.withZoneSameInstant(istZone);
            LocalDateTime loginTimeIST = istDateTime.toLocalDateTime();

            String loginTimeConvention = loginTimeIST.getHour() < 12 ? "AM" : "PM"; // Determine AM/PM based on the hour

            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                LocalDate activityDate = request.getDate();

                DailyActivity dailyActivity = new DailyActivity(user, activityDate, loginTimeIST, null, true, null);
                dailyActivity.setDayOfWeek(activityDate.getDayOfWeek().toString());
                dailyActivity.setLoginTimeConvention(loginTimeConvention); // Set AM/PM

                DailyActivity savedActivity = dailyActivityRepository.save(dailyActivity);

                DailyActivityResponse response = new DailyActivityResponse();
                response.setId(savedActivity.getId());
                response.setUserEmail(request.getEmail());
                response.setDate(request.getDate());
                response.setLoginTime(loginTimeIST); // Store in IST
                response.setPresent(true);
                response.setDayOfWeek(savedActivity.getDayOfWeek());
                response.setAttendanceType(savedActivity.getAttendanceType());
                response.setLoginTimeConvention(loginTimeConvention); // Set AM/PM in response
                return response;
            } else {
                throw new IllegalArgumentException("User with email " + request.getEmail() + " not found");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please provide the date in yyyy-MM-dd format.");
        }
    }


}
