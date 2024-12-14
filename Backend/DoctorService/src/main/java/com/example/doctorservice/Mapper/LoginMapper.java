// LoginMapper.java
package com.example.doctorservice.Mapper;

import com.example.doctorservice.DTO.LoginRequest;
import com.example.doctorservice.Model.Admin;
import com.example.doctorservice.Model.Doctor;

public class LoginMapper {
    public static Admin loginToAdmin(LoginRequest request) {
        return Admin.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    public static Doctor loginToDoctor(LoginRequest request){
        return Doctor.builder()
                .email(request.getUsername())
                .password(request.getPassword())
                .build();
    }
}