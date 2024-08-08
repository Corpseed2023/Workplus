package com.example.workplus.service;

import com.example.workplus.requestDTO.gapRequest.GapTrackRequest;
import com.example.workplus.responseDTO.gapResponse.GapEditTimeResponse;
import com.example.workplus.responseDTO.gapResponse.GapTrackSaveResponse;
import com.example.workplus.responseDTO.gapResponse.GapUserResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface GapTrackService {




    GapTrackSaveResponse saveGapTrack(GapTrackRequest gapTrackRequest);

    GapUserResponse getUserActivity(String userEmail, LocalDate date);

    void updateUserGapReason(String userEmail, Long lastOfflineId, String gapReason,Long lastOnlineId);

    void removeGap(String userEmail, Long lastOfflineId, String reason, Long lastOnlineId);

    void updateTimeUserGapReason(String userEmail, LocalDateTime startTime, String reason, LocalDateTime lastTime, LocalDate date);

    GapEditTimeResponse getUserGapData(String userEmail, LocalDate currentDate);


//    void updateTimeUserGapReason(String userEmail, LocalDateTime startTime, String reason, LocalDateTime lastTime);
}