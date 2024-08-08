package com.example.workplus.responseDTO.gapResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GapTimeEditDetails {

    private Long lastOfflineId;
    private LocalDateTime lastOfflineTime;
    private Long lastOnlineId;
    private LocalDateTime lastOnlineTime;
    private String gapTime;
    private String reason;
    private boolean availability;
    private String filledTime;

}
