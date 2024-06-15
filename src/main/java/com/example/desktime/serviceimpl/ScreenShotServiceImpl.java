package com.example.desktime.serviceimpl;


import com.example.desktime.config.AzureBlobAdapter;
import com.example.desktime.controller.DailyActivityController;
import com.example.desktime.model.Screenshot;
import com.example.desktime.model.User;
import com.example.desktime.repository.ScreenshotRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    Long referenceId;


    public final String PROD_PATH="https://recordplus.blob.core.windows.net/test/";


//
//    @Override
//    public ScreenshotResponse uploadScreenshot(byte[] screenshotData, String userMail, String originalFilename) throws IOException {
//        User user = userRepository.findUserByEmail(userMail);
//        if (user == null) {
//            throw new IllegalArgumentException("User not found with email: " + userMail);
//        }
//
//        // Check if an image with the same name and creation date already exists
//        Screenshot existingScreenshot = screenshotRepository.findByScreenshotNameAndCreatedAt(originalFilename, new Date());
//
//        if (existingScreenshot != null) {
//            // Return a response indicating that the image already exists
//            return mapScreenshotToResponse(existingScreenshot);
//        }
//
//        // Save the screenshot file on the computer drive
//        String directoryPath = "D:/DeskTimeSnap/uploadedScreenshots";
//        Files.createDirectories(Paths.get(directoryPath));
//
//        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String fileName = user.getUsername() + "_" + timestamp + "_" + originalFilename;
//        Path filePath = Paths.get(directoryPath, fileName);
//        Files.write(filePath, screenshotData);
//
//        String imageUrl = "file:///D:/DeskTimeSnap/uploadedScreenshots/" + fileName;
//
//        // Set the date field to the current date
//        LocalDate currentDate = LocalDate.now();
//
//        Screenshot screenshot = new Screenshot(user, currentDate, new Date(), imageUrl, originalFilename);
//        screenshot.setCreatedAt(new Date());
//        screenshot.setUpdatedAt(new Date());
//
//        // Save the screenshot entity in the database
//        Screenshot savedScreenshot = screenshotRepository.save(screenshot);
//
//        return mapScreenshotToResponse(savedScreenshot);
//    }
//
//
//
//    // Utility method to map Screenshot entity to ScreenshotResponse DTO
//    private ScreenshotResponse mapScreenshotToResponse(Screenshot screenshot) {
//        ScreenshotResponse response = new ScreenshotResponse();
//        response.setScreenshotTime(screenshot.getScreenshotTime());
//        response.setScreenshotUrl(screenshot.getScreenshotUrl());
//        response.setScreenshotName(screenshot.getScreenshotName());
//        response.setCreatedAt(screenshot.getCreatedAt());
//        response.setUpdatedAt(screenshot.getUpdatedAt());
//
//        return response;
//    }


    @Override
    public ScreenshotResponse uploadScreenshot(byte[] screenshotData, String userMail, String originalFilename) throws IOException {
        return null;
    }

    @Override
    public List<ScreenshotResponse> getUserScreenshotsByEmailAndDate(String userEmail, LocalDate date) {
        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }

        List<Screenshot> screenshots = screenshotRepository.findByUserAndScreenshotTime(user, date);


        if (screenshots.isEmpty()) {
            throw new IllegalArgumentException("No screenshots found for the user " + userEmail + " on " + date);
        }
        return screenshots.stream()
                .map(this::mapScreenshotToResponseAll)
                .collect(Collectors.toList());
    }


    private ScreenshotResponse mapScreenshotToResponseAll(Screenshot screenshot) {
        return new ScreenshotResponse(
                screenshot.getId(),
                screenshot.getUser().getId(),
                screenshot.getUser().getEmail(),
                screenshot.getScreenshotTime(),
                screenshot.getScreenshotUrl(),
                screenshot.getDate(),
                screenshot.getScreenshotName(),
                screenshot.getCreatedAt(),
                screenshot.getUpdatedAt()

        );
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

    @Override
    public ScreenshotResponse uploadScreenshotV2(MultipartFile file, String userMail, String originalFilename) {
        User user = userRepository.findUserByEmail(userMail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userMail);
        }

        String s = azureAdapter.uploadv2(file, 0);
        String filePath = PROD_PATH + s;

        Screenshot screenshot = new Screenshot();
        screenshot.setUser(user);
        screenshot.setDate(LocalDate.now());
        screenshot.setScreenshotTime(new Date());
        screenshot.setScreenshotUrl(filePath);
        screenshot.setScreenshotName(s);
        screenshot.setCreatedAt(new Date());
        screenshot.setUpdatedAt(new Date());

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
