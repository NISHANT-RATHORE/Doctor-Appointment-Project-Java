package com.example.appointmentservice.Client;

import com.example.appointmentservice.DTO.Doctor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "DoctorService", url = "https://user-service.ddns.net/doctor")
public interface DoctorServiceClient {
    @GetMapping("/getDoctor")
    ResponseEntity<Doctor> getDoctor(@RequestParam String username);

    @PutMapping("/slotBooked")
    ResponseEntity<String> bookingSlots(@RequestBody Doctor request);

    @GetMapping("/getDoctorData")
    ResponseEntity<List<Doctor>> getDoctorsData();
}