package com.example.desktime.requestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    private String username;
    private Long updatedBy;
    private Date updatedAt;
    private boolean isEnable;
    private String roleNames;

}
