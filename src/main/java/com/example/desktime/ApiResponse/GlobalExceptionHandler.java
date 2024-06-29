//package com.example.desktime.ApiResponse;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//
//    @ExceptionHandler(UnauthorizedException.class)
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//
//    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
//        ErrorResponse errorResponse = ErrorResponse.unauthorizedAccess();
//        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
//    }
//}