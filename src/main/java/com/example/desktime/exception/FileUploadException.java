package com.example.desktime.exception;

public class FileUploadException extends SpringBootFileUploadException{


    public FileUploadException(String message) {
        super(message);
    }
}