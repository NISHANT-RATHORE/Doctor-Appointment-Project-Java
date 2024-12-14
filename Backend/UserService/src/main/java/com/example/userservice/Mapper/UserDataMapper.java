package com.example.userservice.Mapper;

import com.example.userservice.DTO.UserDataRequest;
import com.example.userservice.Model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserDataMapper {
    public static UserDataRequest mapToUserDataRequest(User user) {
        return UserDataRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .address1(user.getAddress1())
                .address2(user.getAddress2())
                .image(user.getImage())
                .gender(user.getGender())
                .dob(user.getDob())
                .build();
    }
}