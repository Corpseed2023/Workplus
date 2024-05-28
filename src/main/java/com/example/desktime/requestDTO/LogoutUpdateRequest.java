package com.example.desktime.requestDTO;

import java.time.LocalDate;

public class LogoutUpdateRequest {

    private String email;
    private LocalDate localDate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
