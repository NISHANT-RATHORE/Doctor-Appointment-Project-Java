package com.example.doctorservice.Model;

import com.example.doctorservice.Enums.UserType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor implements UserDetails {
    @Id
    ObjectId id;

    String name;
    String email;
    String password;
    String speciality;
    String degree;
    String experience;
    String about;
    Boolean Available;
    Integer fees;
    String address1;
    String address2;
    Date date;
    UserType userType;
    String authorities;
    List<String> slots;
    String docImg;

    public Doctor() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> authorities);
    }

    @Override
    public String getUsername() {
        return email;
    }
}