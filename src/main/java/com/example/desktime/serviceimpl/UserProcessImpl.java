package com.example.desktime.serviceimpl;

import com.example.desktime.model.User;
import com.example.desktime.model.UserProcess;
import com.example.desktime.repository.UserProcessRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.UserProcessRequest;
import com.example.desktime.responseDTO.UserProcessResponse;
import com.example.desktime.service.UserProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProcessImpl implements UserProcessService {

    @Autowired
    private UserProcessRepository userProcessRepository;

    @Autowired
    private UserRepository userRepository;

    public UserProcessResponse saveUserProcess(UserProcessRequest userProcessRequest) {
        // Check if the user exists based on email

        User user = userRepository.findUserByEmail(userProcessRequest.getUserMail());
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userProcessRequest.getUserMail());
        }

        // Create a new UserProcess object
        UserProcess userProcess = new UserProcess();
        userProcess.setUser(user);
        userProcess.setDate(userProcessRequest.getDate());
        userProcess.setProcessName(userProcessRequest.getProcessName());
        userProcess.setStartTime(userProcessRequest.getStartTime());
        userProcess.setEndTime(userProcessRequest.getEndTime());
        userProcess.setDurationMinutes(userProcessRequest.getDurationMinutes());
        userProcess.setProcessPath(userProcessRequest.getProcessPath());
        userProcess.setDeviceName(userProcessRequest.getDeviceName());
        userProcess.setOperatingSystem(userProcessRequest.getOperatingSystem());
        userProcess.setProcessId(userProcessRequest.getProcessId());
        userProcess.setProcessType(userProcessRequest.getProcessType());
        userProcess.setActivityType(userProcessRequest.getActivityType());
        userProcess.setAdditionalMetadata(userProcessRequest.getAdditionalMetadata());

        // Save the UserProcess
        UserProcess savedUserProcess = userProcessRepository.save(userProcess);

        // Create a UserProcessResponse
        UserProcessResponse userProcessResponse = new UserProcessResponse();
        userProcessResponse.setId(savedUserProcess.getId());
        userProcessResponse.setUserId(savedUserProcess.getUser().getId());
        userProcessResponse.setUserMail(savedUserProcess.getUser().getEmail());

        return userProcessResponse;
    }


    @Override
    public List<UserProcessResponse> getUserProcessesByEmail(String userEmail) {
        List<UserProcess> userProcesses = userProcessRepository.findByUserEmail(userEmail);
        if (userProcesses.isEmpty()) {
            throw new IllegalArgumentException("No user processes found for the user with email: " + userEmail);
        }
        return userProcesses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private UserProcessResponse convertToResponse(UserProcess userProcess) {
        UserProcessResponse response = new UserProcessResponse();
        response.setId(userProcess.getId());
        response.setUserId(userProcess.getUser().getId());
        response.setUserMail(userProcess.getUser().getEmail());
        response.setDate(userProcess.getDate());
        response.setProcessName(userProcess.getProcessName());
        response.setStartTime(userProcess.getStartTime());
        response.setEndTime(userProcess.getEndTime());
        response.setDurationMinutes(userProcess.getDurationMinutes());
        response.setProcessPath(userProcess.getProcessPath());
        response.setDeviceName(userProcess.getDeviceName());
        response.setOperatingSystem(userProcess.getOperatingSystem());
        response.setProcessId(userProcess.getProcessId());
        response.setProcessType(userProcess.getProcessType());
        response.setActivityType(userProcess.getActivityType());
        response.setAdditionalMetadata(userProcess.getAdditionalMetadata());
        return response;
    }


}
