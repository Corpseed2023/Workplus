package com.example.desktime.serviceimpl;

import com.example.desktime.ApiResponse.DataNotFoundException;
import com.example.desktime.model.GapTrack;
import com.example.desktime.model.User;
import com.example.desktime.repository.GapRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.GapTrackResponse;
import com.example.desktime.responseDTO.GapTrackSaveResponse;
import com.example.desktime.responseDTO.GapTrackUpdateResponse;
import com.example.desktime.service.GapTrackService;
import com.example.desktime.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.desktime.ApiResponse.UserNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GapTrackServiceImpl implements GapTrackService {

    @Autowired
    private GapRepository gapRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public GapTrackSaveResponse saveGapTrack(GapTrackRequest gapTrackRequest) {
        User user = userRepository.findUserByEmail(gapTrackRequest.getUserEmail());

        // Check if user exists
        if (user == null) {
            throw new UserNotFoundException();
        }

        LocalDateTime currentIndiaTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        Instant currentIndiaInstant = currentIndiaTime.atZone(ZoneId.of("Asia/Kolkata")).toInstant();
        Date currentIndiaDate = Date.from(currentIndiaInstant);

        GapTrack gapTrack = new GapTrack();
        gapTrack.setUser(user);
        gapTrack.setGapStartTime(currentIndiaDate);
        gapTrack.setWorkingStatus(gapTrackRequest.getStatus());
        gapTrack.setGapEndTime(currentIndiaDate);
        gapTrack.setDate(LocalDate.now());
        gapTrack.setWorkingStatus(gapTrackRequest.getStatus());

        GapTrack savedGapTrack = gapRepository.save(gapTrack);

        // Convert to response DTO
        GapTrackSaveResponse response = new GapTrackSaveResponse();
        response.setId(savedGapTrack.getId());
        response.setUserId(savedGapTrack.getUser().getId());
        response.setDate(savedGapTrack.getDate());
        response.setGapStartTime(savedGapTrack.getGapStartTime());
        response.setGapEndTime(savedGapTrack.getGapEndTime());
        response.setReason(savedGapTrack.getReason());
        response.setGapTime(savedGapTrack.getGapTime());
        response.setWorkingStatus(savedGapTrack.getWorkingStatus());
        response.setAvailability(savedGapTrack.getAvailability());

        return response;
    }


//    @Transactional
    public GapTrackUpdateResponse updateGapTrack(String status, String userMail, LocalDate date) {
        User user = userRepository.findUserByEmail(userMail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<GapTrack> availabilityDataList = gapRepository.findLastAvailability(user, date);

        if (availabilityDataList == null || availabilityDataList.isEmpty()) {
            throw new DataNotFoundException("Data not found for the given user and date");
        }

        GapTrack availabilityData = availabilityDataList.get(0);


        Date currentTime = CommonUtil.getCurrentTimeInIndia();
        availabilityData.setGapEndTime(currentTime);
        availabilityData.setAvailability(true);
        availabilityData.setWorkingStatus(status);

        if (availabilityData.getGapStartTime() != null) {
            long diffInMillies = Math.abs(currentTime.getTime() - availabilityData.getGapStartTime().getTime());
            long diffInMinutes = diffInMillies / (60 * 1000);

            if (diffInMinutes == 0) {
                availabilityData.setGapTime("5 minutes");
            } else {
                availabilityData.setGapTime(diffInMinutes + " minutes");
            }
        }

        GapTrack updatedGap = gapRepository.save(availabilityData);

        GapTrackUpdateResponse response = new GapTrackUpdateResponse();
        response.setId(updatedGap.getId());
        response.setUserId(updatedGap.getUser().getId());
        response.setDate(updatedGap.getDate());
        response.setGapStartTime(updatedGap.getGapStartTime());
        response.setGapEndTime(updatedGap.getGapEndTime());
        response.setReason(updatedGap.getReason());
        response.setGapTime(updatedGap.getGapTime());
        response.setWorkingStatus(updatedGap.getWorkingStatus());
        response.setAvailability(updatedGap.getAvailability());

        return response;
    }



    @Override
    public List<GapTrackResponse> getUserGapData(String userMailId, LocalDate date) {
        User user = userRepository.findUserByEmail(userMailId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<GapTrack> gapTracks = gapRepository.fetchUserGapData(user, date);

        if (gapTracks.isEmpty()) {
            // Handle the scenario where no data is found for the given date
            throw new DataNotFoundException("No gap data found for user on the given date");
        }

//        gapTracks.forEach(gapTrack -> System.out.println("Gap Data: " + gapTrack));

        return gapTracks.stream().map(gapTrack -> {
            GapTrackResponse gapTrackResponse = new GapTrackResponse();
            gapTrackResponse.setId(gapTrack.getId());
            gapTrackResponse.setUserId(user.getId());
            gapTrackResponse.setDate(gapTrack.getDate());
            gapTrackResponse.setGapStartTime(gapTrack.getGapStartTime());
            gapTrackResponse.setGapEndTime(gapTrack.getGapEndTime());
            gapTrackResponse.setReason(gapTrack.getReason());
            gapTrackResponse.setGapTime(gapTrack.getGapTime());
            gapTrackResponse.setWorkingStatus(gapTrack.getWorkingStatus());
            gapTrackResponse.setAvailability(gapTrack.getAvailability());
            return gapTrackResponse;
        }).collect(Collectors.toList());
    }






}
