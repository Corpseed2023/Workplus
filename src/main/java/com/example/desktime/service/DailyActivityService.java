package com.example.desktime.service;

import com.example.desktime.model.DailyActivity;

import java.time.LocalDate;

public interface DailyActivityService {
    void saveDailyActivity(String email, LocalDate activityDate);
}
