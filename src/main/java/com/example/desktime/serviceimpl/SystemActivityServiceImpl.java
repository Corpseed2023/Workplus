package com.example.desktime.serviceimpl;

import com.example.desktime.model.SystemActivity;
import com.example.desktime.repository.SystemActivityRepository;
import com.example.desktime.service.SystemActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemActivityServiceImpl implements SystemActivityService {


    @Autowired
    private SystemActivityRepository systemActivityRepository;

    @Override
    public void logSystemActivity(SystemActivity systemActivity) {
        systemActivityRepository.save(systemActivity);
    }
}
