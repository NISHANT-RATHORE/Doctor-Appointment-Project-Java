package com.example.userservice.DTO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    String email;
    String phone;
    String address1;
    String address2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dob;
    String gender;
}
