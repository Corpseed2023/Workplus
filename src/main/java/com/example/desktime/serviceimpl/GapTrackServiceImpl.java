package com.example.desktime.serviceimpl;

import com.example.desktime.ApiResponse.DataNotFoundException;
import com.example.desktime.model.GapTrack;
import com.example.desktime.model.User;
import com.example.desktime.repository.GapRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.DailyActivityReportResponse;
import com.example.desktime.service.GapTrackService;
import com.example.desktime.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.desktime.ApiResponse.UserNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


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
    public List<GapTrack> fetchUserGapData(Long userId, LocalDate date) {

        User user = userRepository.findByUserId(userId);

        if (user==null)
        {
            throw new UserNotFoundException();

        }




        return null;
    }
}
