package com.example.desktime.serviceimpl;

import com.example.desktime.model.GapTrack;
import com.example.desktime.model.User;
import com.example.desktime.repository.GapRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.service.GapTrackService;
import com.example.desktime.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.desktime.ApiResponse.UserNotFoundException;

import java.time.LocalDate;
import java.util.Date;


@Service
public class GapTrackServiceImpl implements GapTrackService {

    @Autowired
    private GapRepository gapRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public GapTrack saveGapTrack(GapTrackRequest gapTrackRequest) {

        User user = userRepository.findUserByEmail(gapTrackRequest.getUserEmail());

        // Check if user exists
        if (user == null) {
            throw new UserNotFoundException();
        }

        GapTrack gapTrack = new GapTrack();
        gapTrack.setUser(user);
        gapTrack.setGapStartTime(CommonUtil.getCurrentTimeInIndia());
        gapTrack.setWorkingStatus(gapTrackRequest.getStatus());
        gapTrack.setGapEndTime(CommonUtil.getCurrentTimeInIndia());
        gapTrack.setDate(LocalDate.now());
        gapTrack.setWorkingStatus("Unavailable");

        return gapRepository.save(gapTrack);
    }


    @Override
    public GapTrack upadateGapTrack(String status, String useMail) {


        User user = userRepository.findUserByEmail(useMail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        GapTrack gapTrack = new GapTrack();
        gapTrack.setUser(user);
        gapTrack.setGapEndTime(CommonUtil.getCurrentTimeInIndia());

        return null;
    }
}
