package com.example.desktime.service;

import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.GapTrackResponse;
import com.example.desktime.responseDTO.GapTrackSaveResponse;
import com.example.desktime.responseDTO.GapUserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface GapTrackService {



    List<GapTrackResponse> getUserGapData(String userMailId, LocalDate date);

    GapTrackSaveResponse saveGapTrack(GapTrackRequest gapTrackRequest);

    GapUserResponse getUserActivity(String userEmail, LocalDate date);

    void updateUserGapReason(String userEmail, Long gapId, String gapReason);

}
