package com.example.workplus.responseDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GapUserResponse  {

    private String userEmail;
    private LocalDate date;
    private List<GapDetail> gapDetails;
    private String userName;
    private String userLoginTime;
    private String lastActiveTime;
}
