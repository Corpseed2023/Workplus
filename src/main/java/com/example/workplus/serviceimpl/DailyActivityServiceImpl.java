package com.example.workplus.serviceimpl;

import com.example.workplus.model.AttendanceType;
import com.example.workplus.model.DailyActivity;
import com.example.workplus.model.User;
import com.example.workplus.repository.DailyActivityRepository;
import com.example.workplus.repository.UserRepository;
import com.example.workplus.requestDTO.DailyActivityRequest;
import com.example.workplus.requestDTO.EditDailyActivityRequest;
import com.example.workplus.requestDTO.LogoutUpdateRequest;
import com.example.workplus.responseDTO.DailyActivityReportResponse;
import com.example.workplus.responseDTO.DailyActivityResponse;
import com.example.workplus.responseDTO.GapUserResponse;
import com.example.workplus.responseDTO.LogoutUpdateResponse;
import com.example.workplus.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class DailyActivityServiceImpl implements DailyActivityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    @Autowired
    private GapTrackServiceImpl gapTrackService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public DailyActivityResponse saveDailyActivity(DailyActivityRequest request) {
        try {
            // Get the current date and time in Indian time zone
            LocalDateTime currentIndiaTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            LocalDate activityDate = currentIndiaTime.toLocalDate();

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

                LocalTime nineAM = LocalTime.of(9, 0);
                LocalTime sixFortyFiveAM = LocalTime.of(6, 45);
                LocalTime nineThirtyAM = LocalTime.of(9, 30);
                LocalTime timeThreshold = LocalTime.of(9, 35);

                // Check if the current time is before 9:00 AM but after 6:45 AM
                if (currentIndiaTime.toLocalTime().isAfter(sixFortyFiveAM) && currentIndiaTime.toLocalTime().isBefore(nineAM)) {
                    throw new IllegalArgumentException("Invalid time.");
                }

                AttendanceType attendanceType = currentIndiaTime.toLocalTime().isAfter(timeThreshold) ? AttendanceType.HALF_DAY : AttendanceType.NORMAL_DAY;

                LocalDateTime loginTime;
                if (currentIndiaTime.toLocalTime().isAfter(nineAM) && currentIndiaTime.toLocalTime().isBefore(nineThirtyAM)) {
                    loginTime = LocalDateTime.of(activityDate, nineThirtyAM);
                } else {
                    loginTime = currentIndiaTime;
                }

                DailyActivity dailyActivity = new DailyActivity(user, activityDate, loginTime, currentIndiaTime, true, attendanceType);
                dailyActivity.setDayOfWeek(activityDate.getDayOfWeek().toString());
                dailyActivity.setLoginTimeConvention(loginTimeConvention);
                dailyActivity.setLogoutTime(currentIndiaTime);
                dailyActivity.setAttendanceType(attendanceType);

                DailyActivity savedActivity = dailyActivityRepository.save(dailyActivity);

                DailyActivityResponse response = new DailyActivityResponse();
                response.setId(savedActivity.getId());
                response.setUserEmail(request.getEmail());
                response.setDate(activityDate);
                response.setLoginTime(loginTime);
                response.setPresent(true);
                response.setDayOfWeek(savedActivity.getDayOfWeek());
                response.setAttendanceType(savedActivity.getAttendanceType());
                response.setLoginTimeConvention(loginTimeConvention);
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
        User userData = userRepository.findUserByEmail(request.getEmail());
        if (userData == null) {
            throw new IllegalArgumentException("User not found with email: " + request.getEmail());
        }

        // Check if the specified date is today's date or a future date
        LocalDate currentDate = LocalDate.now();
        if (request.getLocalDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Cannot update logout time for a previous date");
        }

        // Check if daily activity data exists for the user and the specified date
        List<DailyActivity> dailyActivities = dailyActivityRepository.findByUserIdAndDate(userData.getId(), request.getLocalDate());

        if (dailyActivities.isEmpty()) {
            throw new IllegalArgumentException("No daily activity found for user with email: " +
                    request.getEmail() + " and date: " + request.getLocalDate());
        }

        // Retrieve the first daily activity from the list
        DailyActivity dailyActivity = dailyActivities.get(0);

        // Update the logout time of daily activity to the current time in India
        LocalDateTime currentIndiaTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        dailyActivity.setLogoutTime(currentIndiaTime);

        // Determine AM/PM based on the current time
        String logoutTimeConvention = currentIndiaTime.getHour() < 12 || (currentIndiaTime.getHour() == 12 &&
                currentIndiaTime.getMinute() == 0) ? "AM" : "PM";

        dailyActivity.setLogoutTimeConvention(logoutTimeConvention);

        // Calculate the duration between loginTime and logoutTime
        if (dailyActivity.getLoginTime() != null && dailyActivity.getAttendanceType() == AttendanceType.NORMAL_DAY) {
            Duration duration = Duration.between(dailyActivity.getLoginTime(), currentIndiaTime);
            long hours = duration.toHours();

            // Update attendanceType to FULL_DAY if the duration is 9 hours or more
            if (hours >= 9) {
                dailyActivity.setAttendanceType(AttendanceType.FULL_DAY);
            }
        }

        dailyActivityRepository.save(dailyActivity);

        return new LogoutUpdateResponse(dailyActivity.getId(), userData.getEmail(), dailyActivity.getLogoutTime());
    }



    @Override
    public DailyActivityResponse getDailyActivityByEmail(String email, LocalDate currentDate) {
        DailyActivity dailyActivity = dailyActivityRepository.findByUserEmailAndDate(email, currentDate);
        DailyActivityResponse response = new DailyActivityResponse();

        if (dailyActivity != null) {
            response = convertToResponse(dailyActivity);
        }

        // Fetch gap data and calculate total gap time
        GapUserResponse gapUserResponse = gapTrackService.getUserGapDataByEmailAndDate(email, currentDate);

        long totalGapMinutes = gapUserResponse.getGapDetails().stream()
                .filter(gap -> !gap.isAvailability())  // Only count gaps where availability is false
                .mapToLong(gap -> Duration.between(gap.getLastOfflineTime(), gap.getLastOnlineTime()).toMinutes())
                .sum();

        long gapHours = totalGapMinutes / 60;
        long gapMinutes = totalGapMinutes % 60;

        // Format the total gap time as "X hours Y minutes"
        String totalGapTime = "";
        if (gapHours > 0) {
            totalGapTime += gapHours + " hours";
            if (gapMinutes > 0) {
                totalGapTime += " ";
            }
        }
        if (gapMinutes > 0) {
            totalGapTime += gapMinutes + " minutes";
        }
        response.setGapTime(totalGapTime);

        if (dailyActivity != null && dailyActivity.getLoginTime() != null) {
            LocalDateTime endTime = dailyActivity.getLogoutTime() != null ? dailyActivity.getLogoutTime() : LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            LocalDateTime loginTime = dailyActivity.getLoginTime();

            long totalMinutesPassed = Duration.between(loginTime, endTime).toMinutes();
            long productiveMinutes = totalMinutesPassed - totalGapMinutes;

            long productiveHours = productiveMinutes / 60;
            long productiveRemainingMinutes = productiveMinutes % 60;

            // Format the productive time as "X hours Y minutes"
            String totalProductiveTime = "";
            if (productiveHours > 0) {
                totalProductiveTime += productiveHours + " hours";
                if (productiveRemainingMinutes > 0) {
                    totalProductiveTime += " ";
                }
            }
            if (productiveRemainingMinutes > 0) {
                totalProductiveTime += productiveRemainingMinutes + " minutes";
            }
            response.setProductiveTime(totalProductiveTime);
        }

        return response;
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

        // Calculate today's total time if login time is present
        if (dailyActivity.getLoginTime() != null) {
            LocalDateTime loginTime = dailyActivity.getLoginTime();
            LocalDateTime currentTime = LocalDateTime.now();

            long minutesPassed;
            if (dailyActivity.getLogoutTime() != null && !dailyActivity.getLogoutTime().equals(loginTime)) {
                minutesPassed = Duration.between(loginTime, dailyActivity.getLogoutTime()).toMinutes();
            } else {
                minutesPassed = Duration.between(loginTime, currentTime).toMinutes();
            }

            long hours = minutesPassed / 60;
            long minutes = minutesPassed % 60;

            // Ensure total time does not exceed 9 hours
            if (hours > 9) {
                hours = 9;
                minutes = 0;
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
                activityResponse.setLoginTime(activity.getLoginTime() != null ? activity.getLoginTime().toLocalTime() : null);
                activityResponse.setLogoutTime(activity.getLogoutTime() != null ? activity.getLogoutTime().toLocalTime() : null);
                activityResponse.setDate(activity.getDate());
                activityResponse.setId(activity.getId());
                activityResponse.setPresent(activity.isPresent() ? "PRESENT" : "ABSENT");

                // Calculate total time if both login and logout times are present
                if (activity.getLoginTime() != null && activity.getLogoutTime() != null) {
                    LocalDateTime loginTime = activity.getLoginTime();
                    LocalDateTime logoutTime = activity.getLogoutTime();

                    Duration duration = Duration.between(loginTime, logoutTime);
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;

                    activityResponse.setTotalTime(hours + " hours " + minutes + " minutes");

                    // Fetch gap data and calculate total gap time for the day
                    GapUserResponse gapUserResponse = gapTrackService.getUserActivity(email, activity.getDate());

                    long totalGapMinutes = gapUserResponse.getGapDetails().stream()
                            .filter(gap -> !gap.isAvailability()) // Only count gaps where availability is false
                            .mapToLong(gap -> Duration.between(gap.getLastOfflineTime(), gap.getLastOnlineTime()).toMinutes())
                            .sum();

                    long gapHours = totalGapMinutes / 60;
                    long gapMinutes = totalGapMinutes % 60;

                    // Format the total gap time as "X hours Y minutes"
                    String totalGapTime = "";
                    if (gapHours > 0) {
                        totalGapTime += gapHours + " hours";
                        if (gapMinutes > 0) {
                            totalGapTime += " ";
                        }
                    }
                    if (gapMinutes > 0) {
                        totalGapTime += gapMinutes + " minutes";
                    }
                    activityResponse.setGapTime(totalGapTime);

                    // Calculate total productive time
                    long totalMinutesPassed = duration.toMinutes();
                    long productiveMinutes = totalMinutesPassed - totalGapMinutes;

                    long productiveHours = productiveMinutes / 60;
                    long productiveRemainingMinutes = productiveMinutes % 60;

                    // Format the productive time as "X hours Y minutes"
                    String totalProductiveTime = "";
                    if (productiveHours > 0) {
                        totalProductiveTime += productiveHours + " hours";
                        if (productiveRemainingMinutes > 0) {
                            totalProductiveTime += " ";
                        }
                    }
                    if (productiveRemainingMinutes > 0) {
                        totalProductiveTime += productiveRemainingMinutes + " minutes";
                    }
                    activityResponse.setProductiveTime(totalProductiveTime);

                } else {
                    activityResponse.setTotalTime("N/A"); // If either login or logout time is missing, set totalTime as "N/A"
                    activityResponse.setProductiveTime("N/A"); // If either login or logout time is missing, set productiveTime as "N/A"
                }

                response.add(activityResponse);
            }

            return response;
        } catch (Exception e) {
            // Log the exception if needed
            throw new RuntimeException("Error retrieving monthly activity report: " + e.getMessage(), e);
        }
    }



    @Override
    public DailyActivityResponse editDailyActivity(EditDailyActivityRequest request, Long userId) {
        try {
            // Check if the user exists in the UserRepository
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }

            User user = userOptional.get();

            // Check if the user has the ADMIN role
            boolean isAdmin = user.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getRoleName()));
            if (!isAdmin) {
                throw new IllegalArgumentException("Only ADMIN users can edit daily activities.");
            }

            // Check if the daily activity exists in the DailyActivityRepository
            Optional<DailyActivity> dailyActivityOptional = dailyActivityRepository.findById(request.getId());
            if (dailyActivityOptional.isPresent()) {
                DailyActivity dailyActivity = dailyActivityOptional.get();

                // Check if the user email matches
                if (!dailyActivity.getUser().getEmail().equals(request.getEmail())) {
                    throw new IllegalArgumentException("Email mismatch for the given activity.");
                }

                // Update fields
                dailyActivity.setDate(request.getDate());
                dailyActivity.setLoginTime(request.getLoginTime());
                dailyActivity.setLogoutTime(request.getLogoutTime());
                dailyActivity.setPresent(request.isPresent());

                // Save the updated activity
                DailyActivity updatedActivity = dailyActivityRepository.save(dailyActivity);

                // Return the updated response
                return convertToResponse(updatedActivity);
            } else {
                throw new IllegalArgumentException("Daily activity not found for the given ID.");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating the daily activity: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getAllUserEmails() {
        return null;
    }

    @Override
    public List<DailyActivityReportResponse> getAllUserMonthlyReport(LocalDate startDate, LocalDate endDate) {
        try {
            List<Object[]> results = dailyActivityRepository.findAllUserMonthlyReport(startDate, endDate);
            if (results == null || results.isEmpty()) {
                return Collections.emptyList();
            }



            List<DailyActivityReportResponse> response = new ArrayList<>();

            for (Object[] result : results) {
                Long id = ((Number) result[0]).longValue();
                LocalDate date = result[1] != null ? ((java.sql.Date) result[1]).toLocalDate() : null;
                LocalDateTime loginTime = result[2] != null ? ((java.sql.Timestamp) result[2]).toLocalDateTime() : null;
                LocalDateTime logoutTime = result[3] != null ? ((java.sql.Timestamp) result[3]).toLocalDateTime() : null;
                boolean present = (boolean) result[4];
                String dayOfWeek = (String) result[5];
                String attendanceType = (String) result[6];
                String username = (String) result[7];
                String email = (String) result[8];

//                GapUserResponse gapUserResponse = gapTrackService.getUserGapDataByEmailAndDate(email, date);

                DailyActivityReportResponse activityResponse = new DailyActivityReportResponse();
                activityResponse.setId(id);
                activityResponse.setDate(date);
                activityResponse.setLoginTime(loginTime!=null ? loginTime.toLocalTime() :null);
                activityResponse.setLogoutTime(logoutTime != null ? logoutTime.toLocalTime() :null);
                activityResponse.setPresent(present ? "PRESENT" : "ABSENT");
                activityResponse.setUserName(username);
                activityResponse.setUserEmail(email);
                activityResponse.setDayOfWeek(dayOfWeek);
                activityResponse.setAttendanceType(attendanceType);

                if (loginTime != null && logoutTime != null) {
                    Duration duration = Duration.between(loginTime, logoutTime);
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    activityResponse.setTotalTime(hours + " hours " + minutes + " minutes");
                } else {
                    activityResponse.setTotalTime("N/A");
                }

                response.add(activityResponse);
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving monthly activity report: " + e.getMessage(), e);
        }
    }


    public List<DailyActivityReportResponse> getUserReportWithdate(LocalDate startDate, LocalDate endDate) {

        try {
                List<Object[]> results = dailyActivityRepository.findAllUserMonthlyReport(startDate, endDate);
                if (results == null || results.isEmpty()) {
                    return Collections.emptyList();
                }

                List<DailyActivityReportResponse> response = new ArrayList<>();

                for (Object[] result : results) {
                    Long id = ((Number) result[0]).longValue();
                    LocalDate date = result[1] != null ? ((java.sql.Date) result[1]).toLocalDate() : null;
                    LocalDateTime loginTime = result[2] != null ? ((java.sql.Timestamp) result[2]).toLocalDateTime() : null;
                    LocalDateTime logoutTime = result[3] != null ? ((java.sql.Timestamp) result[3]).toLocalDateTime() : null;
                    boolean present = (boolean) result[4];
                    String dayOfWeek = (String) result[5];
                    String attendanceType = (String) result[6];
                    String username = (String) result[7];
                    String email = (String) result[8];

                   GapUserResponse gapUserResponse = gapTrackService.getUserGapDataByEmailAndDate(email, date);


                    long totalGapMinutes = gapUserResponse.getGapDetails().stream()
                            .filter(gap -> !gap.isAvailability()) // Only count gaps where availability is false
                            .mapToLong(gap -> Duration.between(gap.getLastOfflineTime(), gap.getLastOnlineTime()).toMinutes())
                            .sum();

                    long gapHours = totalGapMinutes / 60;
                    long gapMinutes = totalGapMinutes % 60;

                    // Format the total gap time as "X hours Y minutes"
                    String totalGapTime = "";
                    if (gapHours > 0) {
                        totalGapTime += gapHours + " hours";
                        if (gapMinutes > 0) {
                            totalGapTime += " ";
                        }
                    }
                    if (gapMinutes > 0) {
                        totalGapTime += gapMinutes + " minutes";
                    }




                    DailyActivityReportResponse activityResponse = new DailyActivityReportResponse();

                    activityResponse.setId(id);
                    activityResponse.setDate(date);
                    activityResponse.setLoginTime(loginTime!=null ? loginTime.toLocalTime() :null);
                    activityResponse.setLogoutTime(logoutTime != null ? logoutTime.toLocalTime() :null);
                    activityResponse.setPresent(present ? "PRESENT" : "ABSENT");
                    activityResponse.setUserName(username);
                    activityResponse.setUserEmail(email);
                    activityResponse.setDayOfWeek(dayOfWeek);
                    activityResponse.setAttendanceType(attendanceType);
                    activityResponse.setGapTime(totalGapTime);

                    if (loginTime != null && logoutTime != null) {
                        Duration duration = Duration.between(loginTime, logoutTime);
                        long hours = duration.toHours();
                        long minutes = duration.toMinutes() % 60;
                        activityResponse.setTotalTime(hours + " hours " + minutes + " minutes");
                    } else {
                        activityResponse.setTotalTime("N/A");
                    }

                    response.add(activityResponse);
                }

                return response;
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving monthly activity report: " + e.getMessage(), e);
            }
        }

}
