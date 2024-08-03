package com.example.workplus.service;

import com.example.workplus.requestDTO.GapTrackRequest;
import com.example.workplus.responseDTO.GapTrackResponse;
import com.example.workplus.responseDTO.GapTrackSaveResponse;
import com.example.workplus.responseDTO.GapUserResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GapTrackService {



    List<GapTrackResponse> getUserGapData(String userMailId, LocalDate date);

    GapTrackSaveResponse saveGapTrack(GapTrackRequest gapTrackRequest);

    GapUserResponse getUserActivity(String userEmail, LocalDate date);

    void updateUserGapReason(String userEmail, Long lastOfflineId, String gapReason,Long lastOnlineId);

    void removeGap(String userEmail, Long lastOfflineId, String reason, Long lastOnlineId);

    void updateTimeUserGapReason(String userEmail, LocalDateTime startTime, String reason, LocalDateTime lastTime);
}
