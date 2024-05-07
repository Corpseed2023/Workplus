package com.example.desktime.serviceimpl;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.model.User;
import com.example.desktime.repository.DailyActivityRepository;
import com.example.desktime.repository.UserProcessRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
import com.example.desktime.responseDTO.DailyActivityReportResponse;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.responseDTO.LogoutUpdateResponse;
import com.example.desktime.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DailyActivityServiceImpl implements DailyActivityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProcessRepository userProcessRepository;


    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public DailyActivityResponse saveDailyActivity(DailyActivityRequest request) {
        try {
            // Get the current date in Indian time zone
            LocalDate activityDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));

            // Get the current time in Indian time zone
            LocalDateTime currentIndiaTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

            // Determine AM/PM based on the current time
            String loginTimeConvention = currentIndiaTime.getHour() < 12 || (currentIndiaTime.getHour() == 12 && currentIndiaTime.getMinute() == 0) ? "AM" : "PM";

            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Check if the data for the specified user and today's date already exists
                Optional<DailyActivity> existingActivity = dailyActivityRepository.findByUserAndDate(user, activityDate);

                // If data already exists for today, return a response indicating that the data was not saved
                if (existingActivity.isPresent()) {
                    throw new IllegalArgumentException("Data already exists for today.");
                }

                DailyActivity dailyActivity = new DailyActivity(user, activityDate, currentIndiaTime, null, true, null);
                dailyActivity.setDayOfWeek(activityDate.getDayOfWeek().toString());
                dailyActivity.setLoginTimeConvention(loginTimeConvention); // Set AM/PM

                DailyActivity savedActivity = dailyActivityRepository.save(dailyActivity);

                DailyActivityResponse response = new DailyActivityResponse();
                response.setId(savedActivity.getId());
                response.setUserEmail(request.getEmail());
                response.setDate(activityDate);
                response.setLoginTime(currentIndiaTime); // Store login time in Indian time zone
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



    @Override
    public LogoutUpdateResponse updateLogoutTime(LogoutUpdateRequest request) {
        String email = request.getEmail();

        // Get the current time in Indian time zone
        LocalDateTime logoutTimeInIndianZone = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<DailyActivity> dailyActivityOptional = dailyActivityRepository.findByUser(user);

            // Check if there's an existing daily activity for the user on the current date
            if (dailyActivityOptional.isPresent()) {
                DailyActivity dailyActivity = dailyActivityOptional.get();

                // Check if login time is present for the current date
                if (dailyActivity.getLoginTime() != null) {
                    // Save the logout time and logout time convention
                    dailyActivity.setLogoutTime(logoutTimeInIndianZone);
                    // Assuming logoutTimeConvention is AM/PM based on the current hour
                    dailyActivity.setLogoutTimeConvention(logoutTimeInIndianZone.getHour() < 12 ? "AM" : "PM");
                    dailyActivityRepository.save(dailyActivity);

                    // Create a response DTO with the updated logout time
                    return new LogoutUpdateResponse(dailyActivity.getId(), email, logoutTimeInIndianZone);
                } else {
                    // If login time is not present for the current date, do not save logout time
                    throw new IllegalStateException("Cannot save logout time. No login time recorded for the user on the current date.");
                }
            } else {
                // If no daily activity exists for the user on the current date, do not save logout time
                throw new IllegalStateException("Cannot save logout time. No daily activity recorded for the user on the current date.");
            }
        } else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }



    @Override
    public DailyActivityResponse getDailyActivityByEmail(String email, LocalDate currentDate) {
        DailyActivity dailyActivity = dailyActivityRepository.findByUserEmailAndDate(email, currentDate);

        DailyActivityResponse response = new DailyActivityResponse();

        if (dailyActivity != null) {
            return convertToResponse(dailyActivity);
        } else {
            return response;
        }
    }

    public DailyActivityResponse convertToResponse(DailyActivity dailyActivity) {
        DailyActivityResponse response = new DailyActivityResponse();
        response.setId(dailyActivity.getId());
        response.setUserEmail(dailyActivity.getUser().getEmail());
        response.setDate(dailyActivity.getDate());
        response.setLoginTime(dailyActivity.getLoginTime());
        response.setLogoutTime(dailyActivity.getLogoutTime());
        response.setPresent(dailyActivity.isPresent());
        response.setDayOfWeek(dailyActivity.getDayOfWeek());
        response.setAttendanceType(dailyActivity.getAttendanceType());
        response.setLoginTimeConvention(dailyActivity.getLoginTimeConvention());
        response.setLogoutTimeConvention(dailyActivity.getLogoutTimeConvention());

        // Calculate today's total time if login time is present
        if (dailyActivity.getLoginTime() != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDate currentDate = LocalDate.now();

            // If the current date is the same as the activity date, calculate elapsed time
            if (currentDate.equals(dailyActivity.getDate())) {
                LocalDateTime loginTime = dailyActivity.getLoginTime();
                long minutesPassed = Duration.between(loginTime, currentTime).toMinutes();
                long hours = minutesPassed / 60;
                long minutes = minutesPassed % 60;

                // Check if the elapsed time exceeds 9 hours

//                if (hours > 9) {
//                    // If elapsed time is more than 9 hours, limit it to 9 hours
//
//                    hours = 9;
//                    minutes = 0; // Reset minutes to ensure total time is exactly 9 hours
//
//                } else if (hours == 9 && minutes > 0) {
//
//                    // If elapsed time is exactly 9 hours and there are extra minutes, adjust to 9 hours
//
//                    minutes = 0;
//                }

                // Calculate today's total time considering the limit
                LocalDateTime todayTotalTime = loginTime.plusHours(hours).plusMinutes(minutes);
                response.setTodayTotalTime(todayTotalTime);
            }
        }

        return response;
    }



    public List<DailyActivityReportResponse> getMonthlyActivityReport(String email, LocalDate startDate, LocalDate endDate) {
        try {
            List<DailyActivity> activities = dailyActivityRepository.findByUserEmailAndDateBetween(email, startDate, endDate);

            if (activities == null || activities.isEmpty()) {
                return Collections.emptyList(); // Return an empty list if no activities found
            }

            List<DailyActivityReportResponse> response = new ArrayList<>();

            for (DailyActivity activity : activities) {
                DailyActivityReportResponse activityResponse = new DailyActivityReportResponse();
                activityResponse.setUserName(activity.getUser().getUsername());
                activityResponse.setUserEmail(activity.getUser().getEmail());
                activityResponse.setLoginTime(activity.getLoginTime());
                activityResponse.setLogoutTime(activity.getLogoutTime());
                activityResponse.setDate(activity.getDate());
                activityResponse.setId(activity.getId());
                activityResponse.setPresent(activity.isPresent());

                // Calculate total time if both login and logout times are present
                if (activity.getLoginTime() != null && activity.getLogoutTime() != null) {
                    LocalDateTime loginTime = activity.getLoginTime();
                    LocalDateTime logoutTime = activity.getLogoutTime();

                    Duration duration = Duration.between(loginTime, logoutTime);
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;

                    activityResponse.setTotalTime(hours + " hours " + minutes + " minutes");
                } else {
                    activityResponse.setTotalTime("N/A"); // If either login or logout time is missing, set totalTime as "N/A"
                }

                response.add(activityResponse);
            }

            return response;
        } catch (Exception e) {
            // Log the exception if needed
            throw new RuntimeException("Error retrieving monthly activity report: " + e.getMessage(), e);
        }
    }





}
