package com.example.appointmentservice.Client;


import com.example.appointmentservice.DTO.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "UserService",url = "https://user-service.ddns.net/user")
public interface UserServiceClient {

    @GetMapping("/getUser")
    ResponseEntity<User> getUserData(@RequestParam String username);

    @GetMapping("/getUserData")
    ResponseEntity<List<User>> getUserData();
}
