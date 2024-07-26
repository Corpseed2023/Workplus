package com.example.workplus.requestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
