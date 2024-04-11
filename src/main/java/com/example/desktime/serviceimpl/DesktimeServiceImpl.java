package com.example.desktime.serviceimpl;

import com.example.desktime.model.Desktime;
import com.example.desktime.model.User;
import com.example.desktime.repository.DesktimeRepository;
import com.example.desktime.requestDTO.DeskTimeRequest;
import com.example.desktime.service.DesktimeService;
import com.example.desktime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesktimeServiceImpl implements DesktimeService {

    @Autowired
    private DesktimeRepository desktimeRepository;

    @Autowired
    private UserService userService;

    @Override
    public void saveDesktimeData(String username, String email, DeskTimeRequest deskTimeRequest) {
        // Check if user exists based on username and email
        User user = userService.getUserByUsernameAndEmail(username, email);
        if (user != null) {
            // User exists, create Desktime object and associate it with the user
            Desktime desktime = new Desktime();
            desktime.setUser(user);
            desktime.setScreenshot(deskTimeRequest.getScreenshot());
            desktime.setProcess(deskTimeRequest.getProcess());
            desktime.setStartDate(deskTimeRequest.getStartDate());
            desktime.setEndDate(deskTimeRequest.getEndDate());
            desktime.setTimeConvention(deskTimeRequest.getTimeConvention());
            desktime.setCurrentDate(deskTimeRequest.getCurrentDate());

            desktimeRepository.save(desktime);
        } else {
            // User not found, throw an exception
            throw new IllegalArgumentException("User not found with provided username and email");
        }
    }

}
