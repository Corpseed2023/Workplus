package com.example.desktime.serviceimpl;

import com.example.desktime.config.EmailService;
import com.example.desktime.config.PasswordConfig;
import com.example.desktime.model.Roles;
import com.example.desktime.model.User;
import com.example.desktime.repository.RolesRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.UserRequest;
import com.example.desktime.requestDTO.UserUpdateRequest;
import com.example.desktime.responseDTO.SingleUserResponse;
import com.example.desktime.responseDTO.UserResponse;
import com.example.desktime.service.UserService;
import jakarta.mail.MessagingException;
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

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    EmailService emailService;

    @Override
    public void saveUserData(UserRequest userRequest) throws AccessDeniedException, MessagingException {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRequest.getRoleNames() != null && userRequest.getRoleNames().contains("ADMIN")) {
            // Create admin user
            User adminUser = new User();
            adminUser.setUsername(userRequest.getUsername());
            adminUser.setEmail(userRequest.getEmail());
            char[] passwordChars = passwordConfig.geek_Password(7);
            System.out.println("User Password Is "+ passwordChars);
            String randomPassword = String.valueOf(passwordChars);
            adminUser.setPassword(passwordEncoder.encode(randomPassword));
            adminUser.setCreatedBy(userRequest.getCreatedBy());
            adminUser.setCreatedAt(new Date());
            adminUser.setUpdatedAt(new Date());
            adminUser.setEnable(userRequest.isEnable());

            Roles adminRole = rolesRepository.findByRoleName("ADMIN");
            if (adminRole == null) {
                throw new NoSuchElementException("ADMIN role not found");
            }
            adminUser.setRoles(Set.of(adminRole));

            userRepository.save(adminUser);

            emailService.sendUserCreationMail(userRequest.getUsername(), randomPassword, userRequest.getEmail());

            return;
        }

        if (!hasAdminRole(userRequest.getCreatedBy())) {
            throw new AccessDeniedException("Only users with ADMIN role can create new users");
        }

        // Create regular user
        User userData = new User();
        userData.setUsername(userRequest.getUsername());
        userData.setEmail(userRequest.getEmail());
        char[] passwordChars = passwordConfig.geek_Password(7);
        String randomPassword = String.valueOf(passwordChars);
        userData.setPassword(passwordEncoder.encode(randomPassword));
        userData.setCreatedBy(userRequest.getCreatedBy());
        userData.setCreatedAt(new Date());
        userData.setUpdatedAt(new Date());
        userData.setEnable(userRequest.isEnable());
        userData.setCreatedAt(userRequest.getCreatedAt());
        userData.setUpdatedAt(userRequest.getUpdatedAt());

        try {
            Set<Roles> roles = getRolesFromRequest(userRequest);
            userData.setRoles(roles);
            userRepository.save(userData);

            emailService.sendUserCreationMail(userRequest.getUsername(), randomPassword, userRequest.getEmail());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user data: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("One or more roles not found");
        } catch (Exception e) {
            throw new RuntimeException("Error saving user data", e);
        }
    }



    private Set<Roles> getRolesFromRequest(UserRequest userRequest) {
        Set<Roles> roles = new HashSet<>();
        if (userRequest.getRoleNames() != null) {
            // Split roleNames string into individual role names
            String[] roleNameArray = userRequest.getRoleNames().split(",");
            for (String roleName : roleNameArray) {
                Roles role = rolesRepository.findByRoleName(roleName.trim());
                if (role == null) {
                    throw new NoSuchElementException("Role not found with name: " + roleName);
                }
                roles.add(role);
            }
        }
        return roles;
    }


    private boolean hasAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        System.out.println(user);


        return user.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
    }



    @Override
    public ResponseEntity<SingleUserResponse> getSingleUserDetails(String usernameMail) {
        User userDetails = userRepository.findUserByEmail(usernameMail);

        if (userDetails != null) {
            SingleUserResponse singleUserResponse = new SingleUserResponse();
            singleUserResponse.setId(userDetails.getId());
            singleUserResponse.setUsername(userDetails.getUsername());
            singleUserResponse.setEmail(userDetails.getEmail());
            singleUserResponse.setCreatedBy(userDetails.getCreatedBy());
            singleUserResponse.setEnable(userDetails.isEnable());
            singleUserResponse.setUpdatedAt(userDetails.getUpdatedAt());
            singleUserResponse.setCreatedAt(userDetails.getCreatedAt());
//             Extract role names from Roles objects and set to SingleUserResponse
//            Set<String> roleNames = userDetails.getRoles().stream()
//                    .map(Roles::getRoleName)
//                    .collect(Collectors.toSet());

            Set<String> roleName = userDetails.getRoles().stream().
                    map(Roles::getRoleName).collect(Collectors.toSet());

            singleUserResponse.setRoles(roleName);

            return ResponseEntity.ok(singleUserResponse);
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
        List<User> users = userRepository.findByIsEnableTrue();
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().stream() // Stream over roles
                                .map(Roles::getRoleName) // Extract roleName
                                .collect(Collectors.toSet()), // Collect roleNames to a Set
                        user.getCreatedAt())) // Include createdAt
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
            return passwordEncoder.matches(rawPassword, encodedPassword);
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
    public void editUserDetails(Long userId, UserUpdateRequest userUpdateRequest) throws AccessDeniedException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User existingUser = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!hasAdminRole(userUpdateRequest.getCreatedBy())) {
            throw new AccessDeniedException("Only users with ADMIN role can edit user details");
        }

        User updatedUser = updateUserFromRequest(existingUser, userUpdateRequest);

        try {
//            Set<Roles> roles = getRole(userUpdateRequest);
//            updatedUser.setRoles(roles);
            userRepository.save(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user data: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("One or more roles not found");
        } catch (Exception e) {
            throw new RuntimeException("Error updating user details", e);
        }
    }

    private User updateUserFromRequest(User existingUser, UserUpdateRequest userRequest) {
        existingUser.setUsername(userRequest.getUsername());
        if (userRequest.getPassword() != null) {
            // Update the password only if it exists in the request
            existingUser.setPassword(userRequest.getPassword());
        }
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



    @Override
    public void initiatePasswordReset(String email, String newPassword) throws MessagingException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            // Send email notification
            emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
        } else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }



}

