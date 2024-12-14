package com.example.doctorservice.Controller;

import com.example.doctorservice.DTO.AddDoctorRequest;
import com.example.doctorservice.DTO.DoctorDataRequest;
import com.example.doctorservice.DTO.LoginRequest;
import com.example.doctorservice.Mapper.LoginMapper;
import com.example.doctorservice.Model.Admin;
import com.example.doctorservice.Model.Doctor;
import com.example.doctorservice.Service.AdminService;
import com.example.doctorservice.Service.DoctorService;
import com.example.doctorservice.Utils.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "${frontend.url2}")
public class AdminController {

    private  final AdminService adminService;
    private final DoctorService doctorService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AdminController(DoctorService doctorService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, AdminService adminService, AdminService adminService1) {
        this.doctorService = doctorService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.adminService = adminService1;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Admin admin = LoginMapper.loginToAdmin(request);
        log.info("Login attempt for user: {}", admin.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = adminService.findByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            log.info("User logged in successfully: {}", admin.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.warn("Login failed for user: {}", admin.getUsername());
            return new ResponseEntity<>("Incorrect username or Password..", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addDoctor")
    public ResponseEntity<?> addDoctor(@RequestPart("docImg") MultipartFile docImg, @RequestPart("doctorData") AddDoctorRequest request) throws IOException {
        log.info("Registering Doctor with {}",request.getEmail());
        Doctor doctor = doctorService.addDoctor(docImg,request);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor already exists");
        }
        log.info("Registered successfully....");
        return ResponseEntity.ok(doctor);
    }

    @PostMapping("/getAllDoctors")
    public ResponseEntity<List<Doctor>> allDoctors() {
        try {
            List<Doctor> allDoctors = doctorService.getAllDoctors();
            if (allDoctors == null || allDoctors.isEmpty()) {
                log.warn("No doctors found");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.emptyList());
            }
            log.info("Retrieved all doctors successfully");
            return ResponseEntity.ok(allDoctors);
        } catch (Exception e) {
            log.error("Error retrieving doctors", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/changeAvailability/{email}")
    public ResponseEntity<?> changeAvailable(@PathVariable String email) {
        try {
            doctorService.changeAvailabilityByEmail(email);
            log.info("Successfully changed the availability");
            return ResponseEntity.status(HttpStatus.OK).body("Successfully changed the availability");
        } catch (Exception e) {
            log.error("Error in changing availability", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in changing availability");
        }
    }


}