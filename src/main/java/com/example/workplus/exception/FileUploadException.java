package com.example.workplus.exception;

public class FileUploadException extends SpringBootFileUploadException{


    public FileUploadException(String message) {
        super(message);
    }
}