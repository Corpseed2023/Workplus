package com.example.workplus.exception;

public class FileEmptyException extends SpringBootFileUploadException {

    public FileEmptyException(String message) {
        super(message);
    }
}
