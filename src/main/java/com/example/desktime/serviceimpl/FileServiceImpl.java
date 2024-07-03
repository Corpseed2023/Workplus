package com.example.desktime.serviceimpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.desktime.config.AzureBlobAdapter;
import com.example.desktime.exception.FileDownloadException;
import com.example.desktime.model.Screenshot;
import com.example.desktime.model.User;
import com.example.desktime.repository.ScreenshotRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.responseDTO.ScreenShotAllResponse;
import com.example.desktime.responseDTO.ScreenshotResponse;
import com.example.desktime.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${aws.bucket.name}")
    private String bucketName;


    private final AmazonS3 s3Client;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScreenshotRepository screenshotRepository;

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;

    public final String PROD_PATH="https://corpseed-workplus.s3.ap-south-1.amazonaws.com/";


    @Override
    public ScreenshotResponse uploadFile(MultipartFile multipartFile, String userMail, String originalFilename) throws IOException {
        User user = userRepository.findUserByEmail(userMail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userMail);
        }

        // Parse the date and time from the file name
        LocalDateTime dateTime = parseDateTimeFromFileName(originalFilename);
        LocalDate date = dateTime.toLocalDate();
        Date screenshotTime = java.sql.Timestamp.valueOf(dateTime);
        Date currentDate = new Date();

        // Convert multipart file to a file
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }

        // Generate file name
        String fileName = generateFileName(multipartFile);

        // Upload file with public read access
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.addUserMetadata("Title", "File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        s3Client.putObject(request);

//        String s = azureBlobAdapter.uploadv2(multipartFile, 0);
//        String filePath = "/" + fileName;

        Screenshot screenshot = new Screenshot();
        screenshot.setUser(user);
        screenshot.setDate(date);
        screenshot.setScreenshotTime(screenshotTime);
        screenshot.setScreenshotUrl(fileName);
        screenshot.setScreenshotName(fileName); // Ensure this matches the uploaded file name
        screenshot.setCreatedAt(currentDate);
        screenshot.setUpdatedAt(currentDate);

        // Save the screenshot
        Screenshot savedScreenshot = screenshotRepository.save(screenshot);

        ScreenshotResponse screenshotResponse = new ScreenshotResponse();
        screenshotResponse.setId(savedScreenshot.getId());
        screenshotResponse.setUserId(savedScreenshot.getUser().getId());
        screenshotResponse.setUserMail(savedScreenshot.getUser().getEmail());
        screenshotResponse.setScreenshotTime(savedScreenshot.getScreenshotTime());
        screenshotResponse.setScreenshotUrl(savedScreenshot.getScreenshotUrl());
        screenshotResponse.setScreenshotName(savedScreenshot.getScreenshotName());
        screenshotResponse.setCreatedAt(savedScreenshot.getCreatedAt());
        screenshotResponse.setUpdatedAt(savedScreenshot.getUpdatedAt());
        screenshotResponse.setDate(savedScreenshot.getDate());

        // Delete file
        file.delete();

        return screenshotResponse;
    }






    @Override
    public Object downloadFile(String fileName) throws FileDownloadException, IOException {
        if (bucketIsEmpty()) {
            throw new FileDownloadException("Requested bucket does not exist or is empty");
        }
        S3Object object = s3Client.getObject(bucketName, fileName);
        try (S3ObjectInputStream s3is = object.getObjectContent()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                byte[] read_buf = new byte[1024];
                int read_len = 0;
                while ((read_len = s3is.read(read_buf)) > 0) {
                    fileOutputStream.write(read_buf, 0, read_len);
                }
            }
            Path pathObject = Paths.get(fileName);
            Resource resource = new UrlResource(pathObject.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileDownloadException("Could not find the file!");
            }
        }
    }

    @Override
    public boolean delete(String fileName) {
        File file = Paths.get(fileName).toFile();
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    private boolean bucketIsEmpty() {
        ListObjectsV2Result result = s3Client.listObjectsV2(this.bucketName);
        if (result == null){
            return false;
        }
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        return objects.isEmpty();
    }

    private String generateFileName(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String baseName = FilenameUtils.getBaseName(file.getOriginalFilename());
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
        return baseName + "_" + timestamp + "." + extension;
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
    public List<ScreenShotAllResponse> getUserScreenshotsByEmailAndDate(String userEmail, LocalDate date) {
        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }

        List<Screenshot> screenshots = screenshotRepository.findByUserAndDate(user, date);

        return screenshots.stream()
                .map(screenshot -> {
                    Instant screenshotTimeInstant = screenshot.getScreenshotTime().toInstant();
                    Instant screenshotTimeIndianInstant = screenshotTimeInstant.plus(5, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES);
                    Date screenshotTimeIndianDate = Date.from(screenshotTimeIndianInstant);

                    String fullScreenshotUrl = PROD_PATH + screenshot.getScreenshotUrl();

                    return new ScreenShotAllResponse(
                            screenshot.getId(),
                            user.getEmail(),
                            screenshot.getDate(),
                            screenshotTimeIndianDate,
                            fullScreenshotUrl,
                            screenshot.getScreenshotName());
                })
                .collect(Collectors.toList());
    }


}