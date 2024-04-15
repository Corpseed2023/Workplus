package com.example.desktime.service;

import com.example.desktime.requestDTO.UserProcessRequest;
import com.example.desktime.responseDTO.UserProcessResponse;

public interface UserProcessService {


    UserProcessResponse saveUserProcess(UserProcessRequest userProcessRequest);

}
