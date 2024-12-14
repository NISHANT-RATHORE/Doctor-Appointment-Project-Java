package com.example.doctorservice.Mapper;

import com.example.doctorservice.DTO.AddAdminRequest;
import com.example.doctorservice.Enums.UserType;
import com.example.doctorservice.Model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {
    public static Admin toAdmin(AddAdminRequest adminDTO) {
        return Admin.builder()
                .name(adminDTO.getName())
                .username(adminDTO.getUsername())
                .password(adminDTO.getPassword())
                .userType(UserType.Admin)
                .authorities("Admin")
                .build();
    }
}