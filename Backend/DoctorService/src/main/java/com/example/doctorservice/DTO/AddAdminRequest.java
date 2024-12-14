package com.example.doctorservice.DTO;

import lombok.Data;

@Data
public class AddAdminRequest {
    private String name;
    private String username;
    private String password;
}