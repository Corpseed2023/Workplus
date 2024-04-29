package com.example.desktime.serviceimpl;


import com.example.desktime.model.Screenshot;
import com.example.desktime.model.User;
import com.example.desktime.repository.ScreenshotRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.responseDTO.ScreenshotResponse;
import com.example.desktime.service.ScreenShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenShotServiceImpl implements ScreenShotService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScreenshotRepository screenshotRepository;

    Long referenceId;


    @Override
    public ScreenshotResponse uploadScreenshot( byte[] screenshotData, String userMail, String originalFilename) throws IOException {


        User user = userRepository.findUserByEmail(userMail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userMail);
        }

        // Save the screenshot file on the computer drive
        String directoryPath = "D:/DeskTimeSnap/uploadedScreenshots"; // Update the directory path as needed
        Files.createDirectories(Paths.get(directoryPath));

        // Append a timestamp to avoid filename conflicts
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = referenceId + "_" + timestamp + "_" + originalFilename; // Use the original filename
        Path filePath = Paths.get(directoryPath, fileName);
        Files.write(filePath, screenshotData);

        // Create the URL for the image (assuming it's served by the application)
        String imageUrl = "file:///D:/DeskTimeSnap/uploadedScreenshots/" + fileName; // Adjust the URL format as needed

        // Create a Screenshot entity
        Screenshot screenshot = new Screenshot();

        screenshot.setUser(user);
        screenshot.setScreenshotTime(new Date());
        screenshot.setScreenshotUrl(imageUrl);
        screenshot.setScreenshotName(originalFilename);
        screenshot.setCreatedAt(new Date());
        screenshot.setUpdatedAt(new Date());

        // Save the screenshot entity in the database
        screenshotRepository.save(screenshot);

        // Convert the entity to a DTO for response
        ScreenshotResponse screenshotResponse = mapScreenshotToResponse(screenshot);

        return screenshotResponse;
    }

    // Utility method to map Screenshot entity to ScreenshotResponse DTO
    private ScreenshotResponse mapScreenshotToResponse(Screenshot screenshot) {
        ScreenshotResponse response = new ScreenshotResponse();
        response.setScreenshotTime(screenshot.getScreenshotTime());
        response.setScreenshotUrl(screenshot.getScreenshotUrl());
        response.setScreenshotName(screenshot.getScreenshotName());
        response.setCreatedAt(screenshot.getCreatedAt());
        response.setUpdatedAt(screenshot.getUpdatedAt());
        return response;
    }


    @Override
    public List<ScreenshotResponse> getUserScreenshotsByEmailAndDate(String userEmail, LocalDate date) {
        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }

        // Get all screenshots for the user on the specified date
        List<Screenshot> screenshots = screenshotRepository.findByUserAndScreenshotTimeBetween(user, date.atStartOfDay(), date.plusDays(1).atStartOfDay());

        if (screenshots.isEmpty()) {
            throw new IllegalArgumentException("No screenshots found for the user " + userEmail + " on " + date);
        }

        // Convert screenshots to response DTOs
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

}
