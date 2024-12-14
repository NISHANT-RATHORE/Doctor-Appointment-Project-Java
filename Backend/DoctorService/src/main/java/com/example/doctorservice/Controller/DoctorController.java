package com.example.doctorservice.Controller;

import com.example.doctorservice.DTO.DoctorDataRequest;
import com.example.doctorservice.Model.Doctor;
import com.example.doctorservice.Service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "${frontend.url1}")
public class DoctorController {
    private static final Logger log = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorService doctorService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public DoctorController(DoctorService doctorService, AuthenticationManager authenticationManager) {
        this.doctorService = doctorService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Doctor>> allDoctors() {
        try {
            List<Doctor> allDoctors = doctorService.getAllDoctors();
            List<Doctor> filteredDoctors = allDoctors.stream()
                    .filter(doctor -> !doctor.getEmail().equals("appointmentService@gmail.com"))
                    .collect(Collectors.toList());
            if (filteredDoctors.isEmpty()) {
                log.warn("No doctors found");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.emptyList());
            }
            log.info("Retrieved all doctors successfully");
            return ResponseEntity.ok(filteredDoctors);
        } catch (Exception e) {
            log.error("Error retrieving doctors", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/getDoctor")
    public ResponseEntity<DoctorDataRequest> getDoctor(@RequestParam String username) {
        try {
            DoctorDataRequest doctor = doctorService.getDoctor(username);
            if (doctor == null) {
                log.warn("No doctor found with username: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            log.info("Retrieved doctor successfully");
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            log.error("Error retrieving doctor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getDoctorData")
    public ResponseEntity<List<DoctorDataRequest>> getDoctorsData(){
        try{
            List<DoctorDataRequest> doctorDataRequests = doctorService.getDoctorsData();
            if (doctorDataRequests == null || doctorDataRequests.isEmpty()) {
                log.warn("No data found for doctors");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.emptyList());
            }
            log.info("Retrieved all doctors data successfully");
            return ResponseEntity.ok(doctorDataRequests);
        } catch (Exception e) {
            log.error("Error retrieving doctors", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PutMapping("/slotBooked")
    public ResponseEntity<String> bookingSlots(@RequestBody DoctorDataRequest request){
        try{
            Doctor doctor = doctorService.updateDoctor(request);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error in updating the patient");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated the profile");
        } catch (Exception e) {
            log.warn(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in updating the doctor");
        }
    }
}
