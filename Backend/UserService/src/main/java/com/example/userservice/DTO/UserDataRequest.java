package com.example.userservice.DTO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataRequest {
    String name;
    String email;
    String password;
    String phone ;
    String address1;
    String address2;
    String image;
    String gender;
    Date dob;
}
