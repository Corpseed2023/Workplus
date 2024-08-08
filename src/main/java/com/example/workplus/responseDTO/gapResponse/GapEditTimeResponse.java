package com.example.workplus.responseDTO.gapResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GapEditTimeResponse {

    private String userEmail;
    private LocalDate date;
    private List<GapTimeEditDetails> gapTimeEditDetails;
    private String userName;
    private String userLoginTime;
    private String lastActiveTime;
}
