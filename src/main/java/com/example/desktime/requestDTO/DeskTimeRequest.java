package com.example.desktime.requestDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class DeskTimeRequest {

    @NotBlank
    private String screenshot;

    @NotBlank
    private String process;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotBlank
    private String timeConvention;

    @NotNull
    private Date currentDate;

    private String description;

    @NotNull
    private Date loginTime;

    @NotBlank
    private String userRole;

    private Date logoutTime;

    @NotBlank
    private String username;

    @NotBlank
    private String loginDate;

    @NotBlank
    private String loginTimeConvention;
}
