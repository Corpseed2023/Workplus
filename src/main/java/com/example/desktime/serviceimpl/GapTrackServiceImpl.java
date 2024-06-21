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

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

        ZonedDateTime currentUTCTime = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime currentIndiaTime = currentUTCTime.plusHours(5).plusMinutes(30);

        GapTrack gapTrack = new GapTrack();
        gapTrack.setUser(user);
        gapTrack.setWorkingStatus(gapTrackRequest.getStatus());
        gapTrack.setDate(currentIndiaTime.toLocalDate());
        gapTrack.setGapStartTime(currentIndiaTime.toLocalDateTime()); // Set gapStartTime

        GapTrack savedGapTrack = gapRepository.save(gapTrack);

        // Convert to response DTO
        GapTrackSaveResponse response = new GapTrackSaveResponse();
        response.setId(savedGapTrack.getId());
        response.setUserId(savedGapTrack.getUser().getId());
        response.setDate(savedGapTrack.getDate());
        response.setGapStartTime(savedGapTrack.getGapStartTime());
        response.setReason(savedGapTrack.getReason());
        response.setWorkingStatus(savedGapTrack.getWorkingStatus());
        response.setAvailability(savedGapTrack.getAvailability());

        return response;
    }


//    public GapTrackUpdateResponse updateGapTrack(String status, String userMail, LocalDate date) {
//        User user = userRepository.findUserByEmail(userMail);
//
//        if (user == null) {
//            throw new UserNotFoundException();
//        }
//
//        List<GapTrack> availabilityDataList = gapRepository.findLastAvailability(user, date);
//
//        if (availabilityDataList == null || availabilityDataList.isEmpty()) {
//            throw new DataNotFoundException("Data not found for the given user and date");
//        }
//
//        GapTrack availabilityData = availabilityDataList.get(0);
//
//        ZonedDateTime currentUTCTime = ZonedDateTime.now(ZoneId.of("UTC"));
//        // Add 5 hours and 30 minutes
//        ZonedDateTime currentIndiaTime = currentUTCTime.plusHours(5).plusMinutes(30);
//        // Update the availability data
//        availabilityData.setAvailability(true);
//        availabilityData.setWorkingStatus(status);
//
//        // Calculate the time difference in minutes
//        if (availabilityData.getGapStartTime() != null) {
//            ZonedDateTime gapStartTime = availabilityData.getGapStartTime().toInstant().atZone(ZoneId.of("Asia/Kolkata"));
//            long diffInMillies = Math.abs(currentIndiaTime.toInstant().toEpochMilli() - gapStartTime.toInstant().toEpochMilli());
//            long diffInMinutes = diffInMillies / (60 * 1000);
//
//            if (diffInMinutes == 0) {
//                availabilityData.setGapTime("5");
//            } else {
//                availabilityData.setGapTime(String.valueOf(diffInMinutes));
//            }
//        }
//
//        GapTrack updatedGap = gapRepository.save(availabilityData);
//
//        // Create response DTO
//        GapTrackUpdateResponse response = new GapTrackUpdateResponse();
//        response.setId(updatedGap.getId());
//        response.setUserId(updatedGap.getUser().getId());
//        response.setDate(updatedGap.getDate());
//        response.setGapStartTime(updatedGap.getGapStartTime());
//        response.setReason(updatedGap.getReason());
//        response.setGapTime(updatedGap.getGapTime());
//        response.setWorkingStatus(updatedGap.getWorkingStatus());
//        response.setAvailability(updatedGap.getAvailability());
//
//        return response;
//    }
//
//


    @Override
    public List<GapTrackResponse> getUserGapData(String userMailId, LocalDate date) {
        User user = userRepository.findUserByEmail(userMailId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<GapTrack> gapTracks = gapRepository.fetchUserGapData(user, date);

        if (gapTracks.isEmpty()) {
            throw new DataNotFoundException("No gap data found for user on the given date");
        }

        List<GapTrackResponse> responseList = new ArrayList<>();

        for (int i = 1; i < gapTracks.size(); i++) {
            GapTrack current = gapTracks.get(i);
            GapTrack previous = gapTracks.get(i - 1);

            // Calculate the offline period
            if ("offline".equals(previous.getWorkingStatus()) && "online".equals(current.getWorkingStatus())) {
                LocalDateTime gapStartTime = previous.getGapStartTime();
                LocalDateTime gapEndTime = current.getGapStartTime(); // Start of the online period

                long offlineDurationMinutes = ChronoUnit.MINUTES.between(gapStartTime, gapEndTime);

                GapTrackResponse gapTrackResponse = new GapTrackResponse();
                gapTrackResponse.setId(current.getId());
                gapTrackResponse.setUserId(user.getId());
                gapTrackResponse.setDate(current.getDate());
                gapTrackResponse.setGapStartTime(gapStartTime);
                gapTrackResponse.setReason(current.getReason());
                gapTrackResponse.setWorkingStatus(previous.getWorkingStatus());
                gapTrackResponse.setAvailability(current.getAvailability());

                responseList.add(gapTrackResponse);
            }
        }

        return responseList;
    }

//    public void updateUserGapReason(String userEmail, Long gapId, String gapReason) {
//        User user = userRepository.findUserByEmail(userEmail);
//
//        if (user != null) {
//            Optional<GapTrack> gapData = gapRepository.findById(gapId);
//
//            if (gapData.isPresent()) {
//                GapTrack gapTrack = gapData.get();
//                gapTrack.setReason(gapReason);
//                gapRepository.save(gapTrack);
//            } else {
//                // Handle the case where the gapId does not exist
////                System.out.println("Gap ID not found: " + gapId);
//            }
//        } else {
//            // Handle the case where the user does not exist
////            System.out.println("User not found: " + userEmail);
//        }
//    }

}
