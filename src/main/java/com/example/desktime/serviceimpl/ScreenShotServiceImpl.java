package com.example.desktime.serviceimpl;


import com.example.desktime.config.AzureBlobAdapter;
import com.example.desktime.controller.DailyActivityController;
import com.example.desktime.model.Screenshot;
import com.example.desktime.model.User;
import com.example.desktime.repository.ScreenshotRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
import com.example.desktime.responseDTO.ScreenShotAllResponse;
import com.example.desktime.responseDTO.ScreenshotResponse;
import com.example.desktime.service.ScreenShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenShotServiceImpl implements ScreenShotService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScreenshotRepository screenshotRepository;

    @Autowired
    private DailyActivityController dailyActivityController;

    @Autowired
    AzureBlobAdapter azureAdapter;


    public final String PROD_PATH="https://corpseed-workplus.s3.ap-south-1.amazonaws.com/";


    @Override
    public ScreenshotResponse uploadScreenshot(byte[] screenshotData, String userMail, String originalFilename) throws IOException {
        return null;
    }

    @Override
    public List<ScreenShotAllResponse> getUserScreenshotsByEmailAndDate(String userEmail, LocalDate date) {
        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }

        List<Screenshot> screenshots = screenshotRepository.findByUserAndDate(user, date);

        return screenshots.stream()
                .map(screenshot -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(screenshot.getScreenshotTime());
                    calendar.add(Calendar.HOUR_OF_DAY, -5);
                    String screenshotTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
                    String imageURL = PROD_PATH + screenshot.getScreenshotUrl();
                    return new ScreenShotAllResponse(
                            screenshot.getId(),
                            user.getEmail(),
                            screenshot.getDate(),
                            screenshotTimeStr,
                            imageURL,
                            screenshot.getScreenshotName());
                })
                .collect(Collectors.toList());
    }




    @Override
    public void deleteScreenshotById(Long screenshotId, Long userId) {
        Screenshot screenshot = screenshotRepository.findById(screenshotId)
                .orElseThrow(() -> new IllegalArgumentException("Screenshot not found with ID: " + screenshotId));

        // Fetch the user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Check if the user has the ADMIN role
        boolean isAdmin = userRepository.existsByIdAndRolesRoleName(userId, "ADMIN");

        if (isAdmin) {
            screenshotRepository.delete(screenshot);
        } else {
            throw new IllegalArgumentException("You do not have permission to delete this screenshot.");
        }
    }

    // Utility method to parse date and time from the file name
    private LocalDateTime parseDateTimeFromFileName(String fileName) {

        String[] parts = fileName.split("_");
        String datePart = parts[1];
        String timePart = parts[2].split("\\.")[0]; // Remove the file extension

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(datePart, dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        LocalTime time = LocalTime.parse(timePart, timeFormatter);

        return LocalDateTime.of(date, time);
    }

    @Override
    public ScreenshotResponse uploadScreenshotV2(MultipartFile file, String userMail, String originalFilename) {
        User user = userRepository.findUserByEmail(userMail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userMail);
        }

        // Parse the date and time from the file name
        LocalDateTime dateTime = parseDateTimeFromFileName(originalFilename);
        LocalDate date = dateTime.toLocalDate();
        Date screenshotTime = java.sql.Timestamp.valueOf(dateTime);
        Date currentDate = new Date();

        String s = azureAdapter.uploadv2(file, 0);
        String filePath = PROD_PATH + s;

        Screenshot screenshot = new Screenshot();
        screenshot.setUser(user);
        screenshot.setDate(date);
        screenshot.setScreenshotTime(screenshotTime);
        screenshot.setScreenshotUrl(filePath);
        screenshot.setScreenshotName(s);
        screenshot.setCreatedAt(currentDate);
        screenshot.setUpdatedAt(currentDate);

        // Save the screenshot
        Screenshot savedScreenshot = screenshotRepository.save(screenshot);

        ScreenshotResponse screenshotResponse = new ScreenshotResponse();
        screenshotResponse.setId(savedScreenshot.getId());
        screenshotResponse.setUserId(savedScreenshot.getUser().getId()); // Assuming getId() returns the user ID
        screenshotResponse.setUserMail(savedScreenshot.getUser().getEmail());
        screenshotResponse.setScreenshotTime(savedScreenshot.getScreenshotTime());
        screenshotResponse.setScreenshotUrl(savedScreenshot.getScreenshotUrl());
        screenshotResponse.setScreenshotName(savedScreenshot.getScreenshotName());
        screenshotResponse.setCreatedAt(savedScreenshot.getCreatedAt());
        screenshotResponse.setUpdatedAt(savedScreenshot.getUpdatedAt());
        screenshotResponse.setDate(savedScreenshot.getDate());

        return screenshotResponse;
    }

}
