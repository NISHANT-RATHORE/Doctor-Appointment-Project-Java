package com.example.appointmentservice.DTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor {
    String name;
    String email;
    String password;
    String speciality;
    String degree;
    String experience;
    String about;
    Boolean Available;
    int fees;
    String address1;
    String address2;
    Date date;
    String authorities;
    List<String> slots;
    String docImg;
}
