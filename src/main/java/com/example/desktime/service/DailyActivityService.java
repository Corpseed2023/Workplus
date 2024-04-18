package com.example.desktime.service;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.responseDTO.LogoutUpdateResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DailyActivityService {


    DailyActivityResponse saveDailyActivity(DailyActivityRequest request);

    LogoutUpdateResponse updateLogoutTime(LogoutUpdateRequest request);

    DailyActivityResponse getDailyActivityByEmail(String email, LocalDate currentDate);
}
