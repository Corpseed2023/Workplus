package com.example.desktime.serviceimpl;

import com.example.desktime.model.Roles;
import com.example.desktime.model.User;
import com.example.desktime.repository.RolesRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.UserRequest;
import com.example.desktime.responseDTO.UserResponse;
import com.example.desktime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public void saveUserData(UserRequest userRequest) {
        User userData = new User();

        userData.setUsername(userRequest.getUsername());
        userData.setEmail(userRequest.getEmail());
        userData.setPassword(userRequest.getPassword());
        userData.setCreatedBy(userRequest.getCreatedBy());
        userData.setCreatedAt(new Date());
        userData.setUpdatedAt(new Date());
        userData.setEnable(userRequest.isEnable());

        try {
            Set<Roles> roles = new HashSet<>();
            if (userRequest.getRoleNames() != null) {
                for (String roleName : userRequest.getRoleNames()) {
                    Roles role = rolesRepository.findByRoleName(roleName);
                    if (role == null) {
                        throw new NoSuchElementException("Role not found with name: " + roleName);
                    }
                    roles.add(role);
                }
            }
            userData.setRoles(roles);

            userRepository.save(userData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user data: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("One or more roles not found");
        } catch (Exception e) {
            throw new RuntimeException("Error saving user data", e);
        }
    }


    @Override
    public ResponseEntity<User> getUserdetails(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.ok(existingUser.get());
        } else {
            // User not found
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public User getUserByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }


    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }
}

