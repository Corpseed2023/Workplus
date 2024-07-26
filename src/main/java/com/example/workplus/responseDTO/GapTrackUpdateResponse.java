package com.example.workplus.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class GapTrackUpdateResponse {
    private Long id;
    private Long userId;
    private LocalDate date;
    private Date gapStartTime;
    private Date gapEndTime;
    private String reason;
    private String gapTime;
    private String workingStatus;
    private Boolean availability;
}
