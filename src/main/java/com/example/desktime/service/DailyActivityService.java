package com.example.desktime.service;

import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.requestDTO.EditDailyActivityRequest;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
import com.example.desktime.responseDTO.DailyActivityReportResponse;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.responseDTO.LogoutUpdateResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface DailyActivityService {


    DailyActivityResponse saveDailyActivity(DailyActivityRequest request);

    LogoutUpdateResponse updateLogoutTime(LogoutUpdateRequest request);

    DailyActivityResponse getDailyActivityByEmail(String email, LocalDate currentDate);

    List<DailyActivityReportResponse> getMonthlyActivityReport(String email, LocalDate startDate, LocalDate endDate);

    DailyActivityResponse editDailyActivity(EditDailyActivityRequest request ,Long userId);

    List<String> getAllUserEmails();

    List<DailyActivityReportResponse> getAllUserMonthlyReport(LocalDate startDate, LocalDate endDate);


    List<DailyActivityReportResponse> getUserReportWithdate(LocalDate startDate, LocalDate endDate);
}
