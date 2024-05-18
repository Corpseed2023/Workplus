package com.example.desktime.service;

import com.example.desktime.model.GapTrack;
import com.example.desktime.requestDTO.GapTrackRequest;

public interface GapTrackService {

    GapTrack saveGapTrack(GapTrackRequest gapTrackRequest);
}
