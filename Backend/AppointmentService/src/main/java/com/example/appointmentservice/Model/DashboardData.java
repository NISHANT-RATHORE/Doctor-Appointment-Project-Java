package com.example.appointmentservice.Model;

import com.example.appointmentservice.DTO.Doctor;
import com.example.appointmentservice.DTO.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardData {
    List<Doctor> doctorsData;
    List<User> userData;
    List<Appointment> appointmentData;
}
