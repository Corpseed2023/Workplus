package com.example.desktime.exception;

public class FileDownloadException extends SpringBootFileUploadException {
    public FileDownloadException(String message) {
        super(message);
    }
}