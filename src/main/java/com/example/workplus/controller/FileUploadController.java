package com.example.workplus.controller;

import com.example.workplus.ApiResponse.APIResponse;
import com.example.workplus.exception.FileDownloadException;
import com.example.workplus.exception.FileEmptyException;
import com.example.workplus.exception.FileUploadException;
import com.example.workplus.responseDTO.ScreenShotAllResponse;
import com.example.workplus.responseDTO.ScreenshotResponse;
import com.example.workplus.service.FileService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
//@RequestMapping("/api/v1/file")
@RequestMapping("/api")

@Validated
public class FileUploadController {
    private final FileService fileService;


    public FileUploadController(FileService fileUploadService) {
        this.fileService = fileUploadService;
    }

    @PostMapping(value = "/uploadScreenShot", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestPart(name = "file", required = false) MultipartFile multipartFile,
                                        @RequestParam(required = false) String userMail) throws FileEmptyException, FileUploadException, IOException {
        if (multipartFile.isEmpty()) {
            throw new FileEmptyException("File is empty. Cannot save an empty file");
        }
        boolean isValidFile = isValidFile(multipartFile);
        List<String> allowedFileExtensions = Arrays.asList("pdf", "txt", "epub", "csv", "png", "jpg", "jpeg", "srt", "PNG", "JPEG");

        String originalFilename = multipartFile.getOriginalFilename(); // Get the original filename
        String fileExtension = FilenameUtils.getExtension(originalFilename);

        if (isValidFile && allowedFileExtensions.contains(fileExtension)) {
            ScreenshotResponse fileName = fileService.uploadFile(multipartFile, userMail, originalFilename);
            APIResponse apiResponse = APIResponse.builder()
                    .message("File uploaded successfully. File unique name => " + fileName)
                    .isSuccessful(true)
                    .statusCode(200)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            APIResponse apiResponse = APIResponse.builder()
                    .message("Invalid File. File extension or File name is not supported")
                    .isSuccessful(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("fileName")  @NotBlank @NotNull String fileName) throws FileDownloadException, IOException, FileDownloadException {
        Object response = fileService.downloadFile(fileName);
        if (response != null){
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(response);
        } else {
            APIResponse apiResponse = APIResponse.builder()
                    .message("File could not be downloaded")
                    .isSuccessful(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("fileName") @NotBlank @NotNull String fileName){
        boolean isDeleted = fileService.delete(fileName);
        if (isDeleted){
            APIResponse apiResponse = APIResponse.builder().message("file deleted!")
                    .statusCode(200).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            APIResponse apiResponse = APIResponse.builder().message("file does not exist")
                    .statusCode(404).build();
            return new ResponseEntity<>("file does not exist", HttpStatus.NOT_FOUND);
        }
    }

    private boolean isValidFile(MultipartFile multipartFile){
        log.info("Empty Status ==> {}", multipartFile.isEmpty());
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }


    @GetMapping("/getUserScreenshotsAWS")
    public ResponseEntity<?> getUserScreenshotsAws(@RequestParam String userEmail, @RequestParam(required = false) String date) {
        try {
            LocalDate screenshotDate;
            if (date != null) {
                screenshotDate = LocalDate.parse(date);
            } else {
                screenshotDate = LocalDate.now();
            }
            List<ScreenShotAllResponse> userScreenshots = fileService.getUserScreenshotsByEmailAndDate(userEmail, screenshotDate);
            return new ResponseEntity<>(userScreenshots, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}