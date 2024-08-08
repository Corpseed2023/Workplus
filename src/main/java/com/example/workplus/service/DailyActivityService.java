package com.example.workplus.service;

import com.example.workplus.requestDTO.DailyActivityRequest;
import com.example.workplus.requestDTO.EditDailyActivityRequest;
import com.example.workplus.requestDTO.LogoutUpdateRequest;
import com.example.workplus.responseDTO.dailActivityResponse.DailyActivityReportResponse;
import com.example.workplus.responseDTO.dailActivityResponse.DailyActivityResponse;
import com.example.workplus.responseDTO.LogoutUpdateResponse;

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
