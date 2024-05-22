package com.example.desktime.service;

import com.example.desktime.model.GapTrack;
import com.example.desktime.requestDTO.GapTrackRequest;

import java.time.LocalDate;
import java.util.List;

public interface GapTrackService {

    GapTrack saveGapTrack(GapTrackRequest gapTrackRequest);

    GapTrack updateGapTrack(String status, String useMail, LocalDate date);

    List<GapTrack> fetchUserGapData(Long userId, LocalDate date);
}
