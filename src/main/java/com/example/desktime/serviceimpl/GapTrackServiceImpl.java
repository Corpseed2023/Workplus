package com.example.desktime.serviceimpl;

import com.example.desktime.ApiResponse.DataNotFoundException;
import com.example.desktime.model.GapTrack;
import com.example.desktime.model.User;
import com.example.desktime.repository.GapRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.GapTrackResponse;
import com.example.desktime.service.GapTrackService;
import com.example.desktime.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.desktime.ApiResponse.UserNotFoundException;
import java.time.LocalDate;
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
        gapTrack.setWorkingStatus(gapTrackRequest.getStatus());

        return gapRepository.save(gapTrack);
    }


    public GapTrack updateGapTrack(String status, String userMail, LocalDate date) {
        User user = userRepository.findUserByEmail(userMail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        GapTrack availabilityData = gapRepository.findLastAvailability(user, date);

        if (availabilityData == null) {
            throw new DataNotFoundException("Data not found for the given user and date");
        }

        Date currentTime = CommonUtil.getCurrentTimeInIndia();
        availabilityData.setGapEndTime(currentTime);
        availabilityData.setAvailability(true);
        availabilityData.setWorkingStatus(status);

        if (availabilityData.getGapStartTime() != null) {
            long diffInMillies = Math.abs(currentTime.getTime() - availabilityData.getGapStartTime().getTime());
            System.out.println("Minus Tim" + diffInMillies);
            long diffInMinutes = diffInMillies / (60 * 1000);
            System.out.println("Minus Tim" + diffInMillies);

            availabilityData.setGapTime(String.valueOf(diffInMinutes) + " minutes");
        }

        GapTrack updatedGap = gapRepository.save(availabilityData);

        return updatedGap;
    }




    @Override
    public List<GapTrackResponse> getUserGapData(Long userId, LocalDate date) {
        User user = userRepository.findEnabledUserById(userId);

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
            GapTrackResponse dto = new GapTrackResponse();
            dto.setUserId(user.getId());
            dto.setDate(gapTrack.getDate());
            dto.setGapStartTime(gapTrack.getGapStartTime());
            dto.setGapEndTime(gapTrack.getGapEndTime());
            dto.setReason(gapTrack.getReason());
            dto.setGapTime(gapTrack.getGapTime());
            dto.setWorkingStatus(gapTrack.getWorkingStatus());
            dto.setAvailability(gapTrack.getAvailability());
            return dto;
        }).collect(Collectors.toList());
    }






}
