package com.example.desktime.serviceimpl;

import com.example.desktime.config.SecurityConfig;
import com.example.desktime.model.Roles;
import com.example.desktime.model.User;
import com.example.desktime.repository.RolesRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.UserRequest;
import com.example.desktime.responseDTO.UserResponse;
import com.example.desktime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUserData(UserRequest userRequest) throws AccessDeniedException {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRequest.getRoleNames() != null && userRequest.getRoleNames().contains("ADMIN")) {
            saveAdminUserData(userRequest);
            return;
        }

        if (!hasAdminRole(userRequest.getCreatedBy())) {
            throw new AccessDeniedException("Only users with ADMIN role can create new users");
        }

        User userData = createUserFromRequest(userRequest);
        try {
            Set<Roles> roles = getRolesFromRequest(userRequest);
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

    private User createUserFromRequest(UserRequest userRequest) {
        User userData = new User();
        userData.setUsername(userRequest.getUsername());
        userData.setEmail(userRequest.getEmail());
        // Encrypting the password before setting it
        userData.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userData.setCreatedBy(userRequest.getCreatedBy());
        userData.setCreatedAt(new Date());
        userData.setUpdatedAt(new Date());
        userData.setEnable(userRequest.isEnable());
        return userData;
    }


    private Set<Roles> getRolesFromRequest(UserRequest userRequest) {
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
        return roles;
    }

    private void saveAdminUserData(UserRequest userRequest) {
        User adminUser = createUserFromRequest(userRequest);
        Roles adminRole = rolesRepository.findByRoleName("ADMIN");
        if (adminRole == null) {
            throw new NoSuchElementException("ADMIN role not found");
        }
        adminUser.setRoles(Set.of(adminRole));
        userRepository.save(adminUser);
    }

    private boolean hasAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        System.out.println(user);


        return user.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
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


    @Override
    public User authenticateUser(String email, String password) {
        // Retrieve user by email
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user != null && passwordMatches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    private boolean passwordMatches(String rawPassword, String encodedPassword) {

        return rawPassword.equals(encodedPassword);
    }

    @Override
    public boolean existofUserDetails(String username, String email) {
        if (username != null && email != null) {
            // Check if both username and email exist
            return userRepository.existsByEmailAndUsername(email,username);
        } else {
            // Check if either username or email exists
            return false;
        }
    }



    @Override
    public void editUserDetails(Long userId, UserRequest userRequest) throws AccessDeniedException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User existingUser = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!hasAdminRole(userRequest.getCreatedBy())) {
            throw new AccessDeniedException("Only users with ADMIN role can edit user details");
        }

        User updatedUser = updateUserFromRequest(existingUser, userRequest);

        try {
            Set<Roles> roles = getRolesFromRequest(userRequest);
            updatedUser.setRoles(roles);
            userRepository.save(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user data: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("One or more roles not found");
        } catch (Exception e) {
            throw new RuntimeException("Error updating user details", e);
        }
    }

    private User updateUserFromRequest(User existingUser, UserRequest userRequest) {
        existingUser.setUsername(userRequest.getUsername());
        existingUser.setPassword(userRequest.getPassword());
        existingUser.setEnable(userRequest.isEnable());
        existingUser.setUpdatedAt(new Date());
        return existingUser;
    }

    @Override
    public void softDeleteUser(Long userId) throws IllegalArgumentException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Set isEnable to false for soft delete
        user.setEnable(false);

        userRepository.save(user);
    }



}

