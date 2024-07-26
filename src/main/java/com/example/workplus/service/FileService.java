package com.example.workplus.service;


import com.example.workplus.exception.FileDownloadException;
import com.example.workplus.exception.FileUploadException;
import com.example.workplus.responseDTO.ScreenShotAllResponse;
import com.example.workplus.responseDTO.ScreenshotResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface FileService {
    ScreenshotResponse uploadFile(MultipartFile multipartFile, String userMail, String originalFilename) throws FileUploadException, IOException;

    Object downloadFile(String fileName) throws FileDownloadException, IOException;

    boolean delete(String fileName);

    List<ScreenShotAllResponse> getUserScreenshotsByEmailAndDate(String userEmail, LocalDate screenshotDate);
}