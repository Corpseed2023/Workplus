package com.example.desktime.requestDTO;



public class DailyActivityRequest {
    private String email;
    private String date;
    private String loginTime;

    // Constructors, getters, and setters


    public DailyActivityRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
