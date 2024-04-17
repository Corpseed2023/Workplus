package com.example.desktime.responseDTO;

import java.time.LocalDateTime;

public class LogoutUpdateResponse {

    private Long id;
    private String userEmail;
    private LocalDateTime logoutTime;

    // Constructors, getters, and setters

    public LogoutUpdateResponse() {
    }

    public LogoutUpdateResponse(Long id, String userEmail, LocalDateTime logoutTime) {
        this.id = id;
        this.userEmail = userEmail;
        this.logoutTime = logoutTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
}
