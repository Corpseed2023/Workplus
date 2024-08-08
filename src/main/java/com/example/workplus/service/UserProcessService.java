package com.example.workplus.service;

import com.example.workplus.requestDTO.UserProcessRequest;
import com.example.workplus.responseDTO.userResponse.UserProcessResponse;

import java.time.LocalDate;
import java.util.List;

public interface UserProcessService {


    UserProcessResponse saveUserProcess(UserProcessRequest userProcessRequest);

    List<UserProcessResponse> getUserProcessesByEmail(String userEmail);

    List<UserProcessResponse> getUserProcessWithEmailAndDate(String userEmail, LocalDate date);
}
