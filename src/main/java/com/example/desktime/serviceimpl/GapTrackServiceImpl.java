package com.example.desktime.serviceimpl;

import com.example.desktime.ApiResponse.DataNotFoundException;
import com.example.desktime.model.GapTrack;
import com.example.desktime.model.User;
import com.example.desktime.repository.GapRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.*;
import com.example.desktime.service.GapTrackService;
import com.example.desktime.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.desktime.ApiResponse.UserNotFoundException;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

    @Override
    public void updateUserGapReason(String userEmail, Long gapId, String gapReason) {

        User user =  userRepository.findUserByEmail(userEmail);

        if (user== null)
        {
            throw  new UserNotFoundException();
        }



    }

    @Override
    public GapUserResponse getUserActivity(String userEmail, LocalDate date) {
        User user = userRepository.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        // Fetch all gap activities for the user on the specified date
        List<GapTrack> gapTracks = gapRepository.fetchUserGapData(user, date);

        // Sort gap activities by gap_start_time to ensure correct sequence
        gapTracks.sort(Comparator.comparing(GapTrack::getGapStartTime));

        Long lastOfflineId = null;
        LocalDateTime lastOfflineTime = null;
        List<GapDetail> gapDetails = new ArrayList<>();

        for (GapTrack gapTrack : gapTracks) {
            if ("offline".equals(gapTrack.getWorkingStatus())) {
                if (lastOfflineTime == null) {
                    lastOfflineId = gapTrack.getId();
                    lastOfflineTime = gapTrack.getGapStartTime();
                }
            } else if ("online".equals(gapTrack.getWorkingStatus())) {
                if (lastOfflineTime != null) {
                    Long lastOnlineId = gapTrack.getId();
                    LocalDateTime lastOnlineTime = gapTrack.getGapStartTime();

                    // Calculate the gap time
                    Duration gapDuration = Duration.between(lastOfflineTime, lastOnlineTime);
                    String gapTime = formatDuration(gapDuration);

                    // Add gap details to the list
                    gapDetails.add(new GapDetail(lastOfflineId, lastOfflineTime, lastOnlineId, lastOnlineTime, gapTime));

                    // Reset the lastOfflineTime and lastOfflineId after pairing with an online event
                    lastOfflineId = null;
                    lastOfflineTime = null;
                }
            }
        }

        // Prepare response DTO
        GapUserResponse response = new GapUserResponse();
        response.setUserEmail(userEmail);
        response.setDate(date);
        response.setGapDetails(gapDetails);

        return response;
    }


    // Helper method to format duration as a string
    private String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        return minutes + " Minutes";
    }







}
