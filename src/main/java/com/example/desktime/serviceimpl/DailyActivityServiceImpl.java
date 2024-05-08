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
        // Check if the user exists
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + request.getEmail());
        }

        // Retrieve the user from Optional
        User user = userOptional.get();

        // Check if the specified date is today's date or a future date
        LocalDate currentDate = LocalDate.now();
        if (request.getLocalDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Cannot update logout time for a previous date");
        }

        // Check if daily activity data exists for the user and the specified date
        Optional<DailyActivity> dailyActivityOptional = dailyActivityRepository.findByUserIdAndDate(user.getId(), request.getLocalDate());

        if (dailyActivityOptional.isEmpty()) {
            throw new IllegalArgumentException("No daily activity found for user with email: " + request.getEmail() + " and date: " + request.getLocalDate());
        }

        // Retrieve the daily activity from Optional
        DailyActivity dailyActivity = dailyActivityOptional.get();

        // Update the logout time of daily activity to the current time in India
        LocalDateTime currentIndiaTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        dailyActivity.setLogoutTime(currentIndiaTime);

        // Determine AM/PM based on the current time
        String logoutTimeConvention = currentIndiaTime.getHour() < 12 || (currentIndiaTime.getHour() == 12 && currentIndiaTime.getMinute() == 0) ? "AM" : "PM";

        // Save the logout time convention
        dailyActivity.setLogoutTimeConvention(logoutTimeConvention);

        // Save the changes to the database
        dailyActivityRepository.save(dailyActivity);

        // Prepare response
        return new LogoutUpdateResponse(dailyActivity.getId(), user.getEmail(), dailyActivity.getLogoutTime());
    }



//    @Override
//    public LogoutUpdateResponse updateLogoutTime(LogoutUpdateRequest request) {
//        String email = request.getEmail();
//
//        // Get the current time in Indian time zone
//        LocalDateTime logoutTimeInIndianZone = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
//
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            Optional<DailyActivity> dailyActivityOptional = dailyActivityRepository.findByUser(user);
//
//            System.out.println("Testing Logout time " + dailyActivityOptional );
//
//            if (dailyActivityOptional.isPresent()) {
//                DailyActivity dailyActivity = dailyActivityOptional.get();
//
//                // Check if login time is present for the current date
//                if (dailyActivity.getLoginTime() != null) {
//                    // Save the logout time and logout time convention
//                    dailyActivity.setLogoutTime(logoutTimeInIndianZone);
//                    // Assuming logoutTimeConvention is AM/PM based on the current hour
//                    dailyActivity.setLogoutTimeConvention(logoutTimeInIndianZone.getHour() < 12 ? "AM" : "PM");
//                    dailyActivityRepository.save(dailyActivity);
//
//                    // Create a response DTO with the updated logout time
//                    return new LogoutUpdateResponse(dailyActivity.getId(), email, logoutTimeInIndianZone);
//                } else {
//                    // If login time is not present for the current date, do not save logout time
//                    throw new IllegalStateException("Cannot save logout time. No login time recorded for the user on the current date.");
//                }
//            } else {
//                // If no daily activity exists for the user on the current date, do not save logout time
//                throw new IllegalStateException("Cannot save logout time. No daily activity recorded for the user on the current date.");
//            }
//        } else {
//            throw new IllegalArgumentException("User not found with email: " + email);
//        }
//    }



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

        // Calculate today's total time if login time and logout time are present
        if (dailyActivity.getLoginTime() != null && dailyActivity.getLogoutTime() != null) {
            LocalDateTime loginTime = dailyActivity.getLoginTime();
            LocalDateTime logoutTime = dailyActivity.getLogoutTime();

            long minutesPassed = Duration.between(loginTime, logoutTime).toMinutes();
            long hours = minutesPassed / 60;
            long minutes = minutesPassed % 60;

            // Ensure total time does not exceed 9 hours
            if (hours > 9) {
                hours = 9;
                minutes = 0;
            }

            // Set today's total time
            response.setTodayTotalTime(LocalDateTime.of(1, 1, 1, (int)hours, (int)minutes));

            // Format the total time as "X hours Y minutes"
            String totalTime = "";
            if (hours > 0) {
                totalTime += hours + " hours";
                if (minutes > 0) {
                    totalTime += " ";
                }
            }
            if (minutes > 0) {
                totalTime += minutes + " minutes";
            }
            response.setLoginTimeToLogoutTime(totalTime);
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
