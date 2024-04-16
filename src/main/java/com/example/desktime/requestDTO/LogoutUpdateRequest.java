package com.example.desktime.requestDTO;

import java.time.LocalDateTime;

public class LogoutUpdateRequest {

    private String email;
    private LocalDateTime logoutTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
}
