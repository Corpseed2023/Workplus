package com.example.desktime.requestDTO;



import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyActivityRequest {
    private String email;
    private LocalDate date;

    public DailyActivityRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
