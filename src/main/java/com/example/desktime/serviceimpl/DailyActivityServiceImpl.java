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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
            String loginTimeConvention = request.getLoginTime().getHour() < 12 ? "AM" : "PM"; // Determine AM/PM based on the hour

            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Check if the data for the specified user and today's date already exists
                Optional<DailyActivity> existingActivity = dailyActivityRepository.findByUserAndDate(user, LocalDate.now());

                // If data already exists for today, return a response indicating that the data was not saved
                if (existingActivity.isPresent()) {
                    throw new IllegalArgumentException("Data already exists for today.");
                }

                LocalDate activityDate = LocalDate.now();

                // Get the current time in Indian time zone
                LocalDateTime currentIndiaTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

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
        LocalDateTime logoutTime = request.getLogoutTime();

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<DailyActivity> dailyActivityOptional = dailyActivityRepository.findByUser(user);
            if (dailyActivityOptional.isPresent()) {
                DailyActivity dailyActivity = dailyActivityOptional.get();
                dailyActivity.setLogoutTime(logoutTime);
                dailyActivityRepository.save(dailyActivity);

                // Create a response DTO with the updated logout time
                return new LogoutUpdateResponse(dailyActivity.getId(), email, logoutTime);
            } else {
                // Create a new daily activity with the provided logout time
                DailyActivity newDailyActivity = new DailyActivity(user, LocalDate.now(), null, logoutTime, false, null);
                dailyActivityRepository.save(newDailyActivity);

                // Create a response DTO with the new daily activity's logout time
                return new LogoutUpdateResponse(newDailyActivity.getId(), email, logoutTime);
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

    private DailyActivityResponse convertToResponse(DailyActivity dailyActivity) {
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
        return response;
    }



// Assuming this is within the DailyActivityService implementation class

    public List<DailyActivityReportResponse> getMonthlyActivityReport(String email, LocalDate startDate, LocalDate endDate) {

        List<DailyActivity> activities = dailyActivityRepository.findByUserEmailAndDateBetween(email, startDate, endDate);

        List<DailyActivityReportResponse> response = new ArrayList<>();
        for (DailyActivity activity : activities) {
            DailyActivityReportResponse activityResponse = new DailyActivityReportResponse();
            activityResponse.setUserName(activity.getUser().getUsername());
            activityResponse.setUserEmail(activity.getUser().getEmail());
            activityResponse.setLoginTime(activity.getLoginTime());
            activityResponse.setLogoutTime(activity.getLogoutTime());
            activityResponse.setDate(activity.getDate());
            activityResponse.setId(activity.getId());
            response.add(activityResponse);
        }

        return response;
    }




}
