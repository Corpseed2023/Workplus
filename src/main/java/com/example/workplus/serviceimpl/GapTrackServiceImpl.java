package com.example.workplus.serviceimpl;

import com.example.workplus.ApiResponse.DataNotFoundException;
import com.example.workplus.model.DailyActivity;
import com.example.workplus.model.GapTrack;
import com.example.workplus.model.User;
import com.example.workplus.repository.DailyActivityRepository;
import com.example.workplus.repository.GapRepository;
import com.example.workplus.repository.UserRepository;
import com.example.workplus.requestDTO.GapTrackRequest;
import com.example.workplus.responseDTO.*;
import com.example.workplus.service.GapTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.workplus.ApiResponse.UserNotFoundException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class GapTrackServiceImpl implements GapTrackService {

    @Autowired
    private GapRepository gapTrackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DailyActivityRepository dailyActivityRepository;


    @Override
    public GapTrackSaveResponse saveGapTrack(GapTrackRequest gapTrackRequest) {
        User user = userRepository.findUserByEmail(gapTrackRequest.getUserEmail());

        if (user == null) {
            throw new UserNotFoundException();
        }

        ZonedDateTime currentUTCTime = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime currentIndiaTime = currentUTCTime.plusHours(5).plusMinutes(30);

        GapTrack gapTrack = new GapTrack();
        gapTrack.setUser(user);
        gapTrack.setWorkingStatus(gapTrackRequest.getStatus());
        gapTrack.setDate(currentIndiaTime.toLocalDate());
        gapTrack.setGapStartTime(currentIndiaTime.toLocalDateTime());
        gapTrack.setProductivity("offline".equals(gapTrackRequest.getStatus()) ? false : true);

        GapTrack savedGapTrack = gapTrackRepository.save(gapTrack);


        if ("online".equals(gapTrackRequest.getStatus())) {
            DailyActivity dailyActivity = dailyActivityRepository.findByUserEmailAndDate(gapTrackRequest.getUserEmail(), currentIndiaTime.toLocalDate());

            if (dailyActivity != null) {
                dailyActivityRepository.updateLogoutTime(gapTrackRequest.getUserEmail(), currentIndiaTime.toLocalDate(), currentIndiaTime.toLocalDateTime());
            }
        }
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


    public void updateUserGapReason(String userEmail, Long lastOfflineId, String gapReason, Long lastOnlineId) {


        User user = userRepository.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<GapTrack> gapTrackData = gapTrackRepository.findByGapId(lastOfflineId, lastOnlineId, user);

        if (gapTrackData != null && !gapTrackData.isEmpty()) {
            // Update the first element
            GapTrack firstGapTrack = gapTrackData.get(0);
            firstGapTrack.setReason(gapReason);

            if (!gapReason.isBlank()) {
                firstGapTrack.setProductivity(true);
                firstGapTrack.setAvailability(true);
            }
            else
            {
                firstGapTrack.setProductivity(false);
                firstGapTrack.setAvailability(false);
            }

            // Update the last element
            GapTrack lastGapTrack = gapTrackData.get(gapTrackData.size() - 1);
            lastGapTrack.setProductivity(true);
            lastGapTrack.setAvailability(true);

            // Save the updated objects
            gapTrackRepository.save(firstGapTrack);
            if (firstGapTrack.getId() != lastGapTrack.getId()) {
                gapTrackRepository.save(lastGapTrack);
            }
        } else {
            throw new DataNotFoundException("Gap Data Not Found");
        }
    }


    public void updateTimeUserGapReason(String userEmail, LocalDateTime startTime, String reason, LocalDateTime endTime, LocalDate date) {
        User user = userRepository.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<GapTrack> gapTrackData = gapTrackRepository.findByStartTimeAndEndTime(startTime, endTime, user,date);

        if (gapTrackData != null && !gapTrackData.isEmpty()) {
            for (GapTrack gapTrack : gapTrackData) {
                gapTrack.setReason(reason);
                gapTrack.setFilledGapStatus(2);
                if (!reason.isBlank()) {
                    gapTrack.setProductivity(true);
                    gapTrack.setAvailability(true);
                } else {
                    gapTrack.setProductivity(false);
                    gapTrack.setAvailability(false);
                }
                gapTrackRepository.save(gapTrack);
            }
        } else {
            throw new DataNotFoundException("Gap Data Not Found");
        }
    }


    @Override
    public GapUserResponse getUserActivity(String userEmail, LocalDate date) {
        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }

        // Fetch all gap activities for the user on the specified date
        List<GapTrack> gapTracks = gapTrackRepository.fetchUserGapData(user, date);

        Optional<DailyActivity> dailyActivity = dailyActivityRepository.findByUserAndDate(user, date);

        // Sort gap activities by gap_start_time to ensure correct sequence
        gapTracks.sort(Comparator.comparing(GapTrack::getGapStartTime));

        Long lastOfflineId = null;
        LocalDateTime lastOfflineTime = null;
        String reason = null;
        List<GapDetail> gapDetails = new ArrayList<>();

        for (int i = 0; i < gapTracks.size(); i++) {
            GapTrack currentGapTrack = gapTracks.get(i);
            if ("offline".equals(currentGapTrack.getWorkingStatus())) {
                if (lastOfflineTime == null) {
                    lastOfflineId = currentGapTrack.getId();
                    lastOfflineTime = currentGapTrack.getGapStartTime();
                    reason = currentGapTrack.getReason();
                }
            } else if ("online".equals(currentGapTrack.getWorkingStatus())) {
                if (lastOfflineTime != null) {
                    Long lastOnlineId = currentGapTrack.getId();
                    LocalDateTime lastOnlineTime = currentGapTrack.getGapStartTime();

                    // Calculate the gap time
                    Duration gapDuration = Duration.between(lastOfflineTime, lastOnlineTime);
                    String gapTime = formatDuration(gapDuration);

                    boolean availability = currentGapTrack.getAvailability();

                    GapDetail gapDetail = new GapDetail(lastOfflineId, lastOfflineTime, lastOnlineId, lastOnlineTime, gapTime, reason, availability);
                    if (gapDuration.toMinutes() > 4) {
                        gapDetails.add(gapDetail);
                    }
                    // Reset the lastOfflineTime, lastOfflineId, and reason after pairing with an online event
                    lastOfflineId = null;
                    lastOfflineTime = null;
                    reason = null;
                }
                // Check for consecutive online statuses with gapStartTime difference > 10 minutes
                if (i > 0) {
                    GapTrack previousGapTrack = gapTracks.get(i - 1);
                    if ("online".equals(previousGapTrack.getWorkingStatus())) {
                        Duration gapDuration = Duration.between(previousGapTrack.getGapStartTime(), currentGapTrack.getGapStartTime());
                        if (gapDuration.toMinutes() > 10) {
                            Long lastOnlineId = currentGapTrack.getId();
                            LocalDateTime lastOnlineTime = currentGapTrack.getGapStartTime();
                            String gapTime = formatDuration(gapDuration);

                            GapDetail gapDetail = new GapDetail(previousGapTrack.getId(), previousGapTrack.getGapStartTime(), lastOnlineId, lastOnlineTime, gapTime, null, currentGapTrack.getAvailability());
                            gapDetails.add(gapDetail);
                        }
                    }
                }
            }
        }

        // Convert LocalDateTime to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loginTimeStr = dailyActivity.map(activity -> activity.getLoginTime().format(formatter)).orElse(null);

        // Prepare response DTO
        GapUserResponse response = new GapUserResponse();
        response.setUserEmail(userEmail);
        response.setDate(date);
        response.setGapDetails(gapDetails);
        response.setUserLoginTime(loginTimeStr);

        // Fetch and set the last gap_start_time
        if (!gapTracks.isEmpty()) {
            LocalDateTime localDateTime = gapTracks.get(gapTracks.size() - 1).getGapStartTime();
            response.setLastActiveTime(localDateTime.format(formatter));
        }

        return response;
    }


    public GapUserResponse getUserGapDataByEmailAndDate(String userEmail, LocalDate date) {

        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            // Log the user not found scenario
            System.out.println("User not found with email: " + userEmail);
            return null; // Return null if the user is not found
        }

        // Fetch all gap activities for the user on the specified date
        List<GapTrack> gapTracks = gapTrackRepository.fetchUserGapData(user, date);
        Optional<DailyActivity> dailyActivity = dailyActivityRepository.findByUserAndDate(user, date);

        // Sort gap activities by gap_start_time to ensure correct sequence
        gapTracks.sort(Comparator.comparing(GapTrack::getGapStartTime));

        Long lastOfflineId = null;
        LocalDateTime lastOfflineTime = null;
        String reason = null;
        List<GapDetail> gapDetails = new ArrayList<>();

        for (GapTrack gapTrack : gapTracks) {
            if ("offline".equals(gapTrack.getWorkingStatus())) {
                if (lastOfflineTime == null) {
                    lastOfflineId = gapTrack.getId();
                    lastOfflineTime = gapTrack.getGapStartTime();
                    reason = gapTrack.getReason();
                }
            } else if ("online".equals(gapTrack.getWorkingStatus())) {
                if (lastOfflineTime != null) {
                    Long lastOnlineId = gapTrack.getId();
                    LocalDateTime lastOnlineTime = gapTrack.getGapStartTime();

                    // Calculate the gap time
                    Duration gapDuration = Duration.between(lastOfflineTime, lastOnlineTime);
                    String gapTime = formatDuration(gapDuration);

                    boolean availability = gapTrack.getAvailability();

                    GapDetail gapDetail = new GapDetail(lastOfflineId, lastOfflineTime, lastOnlineId, lastOnlineTime, gapTime, reason, availability);
                    if (gapDuration.toMinutes() > 4) {
                        gapDetails.add(gapDetail);
                    }
                    // Reset the lastOfflineTime, lastOfflineId, and reason after pairing with an online event
                    lastOfflineId = null;
                    lastOfflineTime = null;
                    reason = null;
                }
            }
        }

        // Convert LocalDateTime to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loginTimeStr = dailyActivity.map(activity -> activity.getLoginTime().format(formatter)).orElse(null);

        // Prepare response DTO
        GapUserResponse response = new GapUserResponse();
        response.setUserEmail(userEmail);
        response.setDate(date);
        response.setGapDetails(gapDetails);
        response.setUserLoginTime(loginTimeStr);

        // Fetch and set the last gap_start_time
        if (!gapTracks.isEmpty()) {
            LocalDateTime localDateTime = gapTracks.get(gapTracks.size() - 1).getGapStartTime();
            response.setLastActiveTime(localDateTime.format(formatter));
        }

        return response;
    }


    // Helper method to format duration as a string
    private String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        return minutes + " Minutes";
    }

    @Override
    public void removeGap(String userEmail, Long lastOfflineId, String reason, Long lastOnlineId) {

        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }
        List<GapTrack> gapTrackData = gapTrackRepository.findByGapId(lastOfflineId, lastOnlineId, user);


        if (!gapTrackData.isEmpty()) {
            updateRemoveGapTrack(gapTrackData.get(0), false, false);

            GapTrack lastGapTrack = gapTrackData.get(gapTrackData.size() - 1);
            updateRemoveGapTrack(lastGapTrack, null, false);
        }
    }

    private void updateRemoveGapTrack(GapTrack gapTrack, Boolean productivity, Boolean availability) {
        if (productivity != null) {
            gapTrack.setProductivity(productivity);
        }
        if (availability != null) {
            gapTrack.setAvailability(availability);
        }

        gapTrack.setReason(null);
        gapTrackRepository.save(gapTrack);
    }
}
