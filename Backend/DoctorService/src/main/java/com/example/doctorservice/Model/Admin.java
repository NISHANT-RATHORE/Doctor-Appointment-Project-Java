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

@Data
@AllArgsConstructor
@Builder
@Document(collection = "admins")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Admin implements UserDetails {
    @Id
    ObjectId id;

    String name;
    String username;
    String password;
    UserType userType;
    String authorities;

    public Admin() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> authorities);
    }

    @Override
    public String getUsername() {
        return username;
    }
}