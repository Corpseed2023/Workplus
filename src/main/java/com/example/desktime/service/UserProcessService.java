package com.example.desktime.service;

import com.example.desktime.requestDTO.UserProcessRequest;
import com.example.desktime.responseDTO.UserProcessResponse;

import java.time.LocalDate;
import java.util.List;

public interface UserProcessService {


    UserProcessResponse saveUserProcess(UserProcessRequest userProcessRequest);

    List<UserProcessResponse> getUserProcessesByEmail(String userEmail);

    List<UserProcessResponse> getUserProcessWithEmailAndDate(String userEmail, LocalDate date);
}
