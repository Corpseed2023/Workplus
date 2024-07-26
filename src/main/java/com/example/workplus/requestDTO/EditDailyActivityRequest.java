package com.example.workplus.requestDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class EditDailyActivityRequest {


    private Long id;
    private String email;
    private LocalDate date;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private boolean present;

}
