package com.example.desktime.serviceimpl;

import com.example.desktime.model.User;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUserData(User user) {
        User userData = new User();

        userData.setUsername(user.getUsername());
        userData.setLoginTime(user.getLoginTime());
        userData.setLogoutTime(user.getLogoutTime());
        userData.setLoginDate(user.getLoginDate());
        userData.setLoginTimeConvention(user.getLoginTimeConvention());

        userRepository.save(userData);
    }


}
