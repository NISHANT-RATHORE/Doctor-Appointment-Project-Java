package com.example.userservice.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUserRequest {


    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    String address1;

    String address2;

    Date dob;

    String gender;

}
