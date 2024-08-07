package com.example.workplus.controller;



import com.example.workplus.model.IPAccess;
import com.example.workplus.model.User;
import com.example.workplus.repository.IpAccessRepository;
import com.example.workplus.requestDTO.DailyActivityRequest;
import com.example.workplus.requestDTO.LoginRequest;
import com.example.workplus.requestDTO.UserRequest;
import com.example.workplus.requestDTO.UserUpdateRequest;
import com.example.workplus.responseDTO.LoginResponse;
import com.example.workplus.responseDTO.SingleUserResponse;
import com.example.workplus.responseDTO.UserResponse;
import com.example.workplus.responseDTO.UserUpdatedResponse;
import com.example.workplus.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IpAccessRepository ipAccessRepository;

    @Autowired
    private DailyActivityController dailyActivityController;



    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            userService.saveUserData(userRequest);

            return new ResponseEntity<>("User created successfully! An email has been sent to the user.", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid user data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("One or more roles not found", HttpStatus.BAD_REQUEST);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>("Access denied: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userDetails")
    public ResponseEntity<SingleUserResponse> getUserDetails(@RequestParam String usernameMail) {
        ResponseEntity<SingleUserResponse> responseEntity = userService.getSingleUserDetails(usernameMail);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/allUsersList")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<UserResponse> users = userService.getAllUsers(page, size);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest serverRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body(new LoginResponse("Email and password are required"));
            }

            // Retrieve user by email
            User authenticatedUser = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (authenticatedUser != null) {
                // Check if the user has the ADMIN role
                boolean isAdmin = authenticatedUser.getRoles().stream()
                        .anyMatch(role -> "ADMIN".equals(role.getRoleName()));

                // If the user is not an ADMIN, check the IP address
                if (!isAdmin) {
                    String networkIp = serverRequest.getHeader("X-Forwarded-For");
                    if (networkIp == null || networkIp.isEmpty()) {
                        networkIp = serverRequest.getRemoteAddr();
                    } else if (networkIp.contains(":")) {
                        networkIp = networkIp.split(":")[0];
                    }

                    IPAccess ip = ipAccessRepository.findByNetworkIpAddress(networkIp);

                    if (ip == null || !ip.getNetworkIpAddress().equals(networkIp)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                }

                // Set roles separately
                Set<String> roles = authenticatedUser.getRoles().stream()
                        .map(role -> role.getRoleName())
                        .collect(Collectors.toSet());

                // Create LoginResponse object with roles
                LoginResponse response = new LoginResponse("Login successful");
                response.setId(authenticatedUser.getId());
                response.setUsername(authenticatedUser.getUsername());
                response.setEmail(authenticatedUser.getEmail());
                response.setRoles(roles);

                DailyActivityRequest dailyActivityRequest = new DailyActivityRequest();
                dailyActivityRequest.setEmail(loginRequest.getEmail());

                dailyActivityController.saveDailyActivity(dailyActivityRequest);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid email or password"));
            }
        } catch (Exception e) {
            // Log exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("Error processing the request"));
        }
    }






    @GetMapping("/checkUserExists")
    public ResponseEntity<Boolean> checkUserExists(@RequestParam(required = false) String username,
                                                   @RequestParam(required = false) String email) {
        if (username == null && email == null) {
            return ResponseEntity.badRequest().body(false); // Return false if both parameters are null
        }

        boolean userExists = false;

        if (username != null && email != null) {
            userExists = userService.existofUserDetails(username, email);
        }

        if (userExists) {
            return ResponseEntity.ok(true); // Both username and email exist
        } else {
            return ResponseEntity.notFound().build(); // Either username or email does not exist
        }
    }


    @PutMapping("/editUser")
    public ResponseEntity<?> editUserDetails(@RequestParam Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            UserUpdatedResponse editedUserDetails= userService.editUserDetails(userId, userUpdateRequest);
            return new ResponseEntity<>(editedUserDetails,HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid user data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("One or more roles not found", HttpStatus.BAD_REQUEST);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>("Only administrators can edit user details", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @DeleteMapping("/deleteUsers")
    public ResponseEntity<String> softDeleteUsers(@RequestBody List<Long> userIds) {
        try {
            userService.softDeleteUsers(userIds);
            return new ResponseEntity<>("Users soft deleted successfully!", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String email ,@RequestParam String password) {
        try {
            userService.initiatePasswordReset(email,password);
            return new ResponseEntity<>("Password reset email sent successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error initiating password reset: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
