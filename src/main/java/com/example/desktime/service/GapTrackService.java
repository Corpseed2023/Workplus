package com.example.desktime.service;

import com.example.desktime.model.GapTrack;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.GapTrackResponse;
import com.example.desktime.responseDTO.GapTrackSaveResponse;
import com.example.desktime.responseDTO.GapTrackUpdateResponse;

import java.time.LocalDate;
import java.util.List;

public interface GapTrackService {



    List<GapTrackResponse> getUserGapData(String userMailId, LocalDate date);

    GapTrackSaveResponse saveGapTrack(GapTrackRequest gapTrackRequest);


    GapTrackUpdateResponse updateGapTrack(String status, String userEmail, LocalDate date);
}
