package com.example.userservice.Controller;

import com.example.userservice.DTO.AddUserRequest;
import com.example.userservice.DTO.LoginRequest;
import com.example.userservice.DTO.UpdateUserRequest;
import com.example.userservice.DTO.UserDataRequest;
import com.example.userservice.Mapper.LoginMapper;
import com.example.userservice.Model.User;
import com.example.userservice.Service.UserService;
import com.example.userservice.Utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "${frontend.url}")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody @Valid AddUserRequest request) throws Exception {
        log.info("Registering user with email: {}", request.getEmail());
        User addedUser = userService.addUser(request);
        return ResponseEntity.ok(addedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {
        User user = LoginMapper.loginToUser(request);
        log.info("Login attempt for user: {}", user.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            log.info("User logged in successfully: {}", user.getEmail());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.warn("Login failed for user: {}", user.getEmail());
            return new ResponseEntity<>("Incorrect username or Password..", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getProfile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String token1) {
        try {
            String token = token1.substring(7);
            String username = jwtUtil.extractUsername(token);
            User user = userService.getUser(username);
            if (user == null) {
                log.warn("No user found");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            log.info("Retrieved user profile successfully");
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error retrieving user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestParam(required = false) MultipartFile image, @ModelAttribute UpdateUserRequest request, @RequestHeader("Authorization") String token1) {
        try {
            String token = token1.substring(7);
            log.info(token);
            String username = jwtUtil.extractUsername(token);
            log.info("Updating profile with {}", username);
            User user = userService.updateProfile(image, username, request);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error in updating the patient");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated the profile");
        } catch (Exception e) {
            log.warn(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserDataRequest> getUserData(@RequestParam String username) {
        try {
            UserDataRequest user = userService.getUserData(username);
            if (user == null) {
                log.warn("No user found with username: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            log.info("Retrieved doctor successfully");
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error retrieving doctor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getUserData")
    public ResponseEntity<List<UserDataRequest>> getUserData(){
        try{
            List<UserDataRequest> user = userService.getallUserData();
            if(user.isEmpty()){
                log.warn("no user found ..");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.emptyList());
            }
            log.info("successfully fetched user Data");
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error retrieving users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}