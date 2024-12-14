package com.example.doctorservice.Configuration;

import com.example.doctorservice.Enums.UserType;
import com.example.doctorservice.Model.Admin;
import com.example.doctorservice.Repository.AdminRepository;
import com.example.doctorservice.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitilalizer implements CommandLineRunner {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Autowired
    public AdminInitilalizer(AdminService adminService, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (adminService.findByUsername("nishant@gmail.com")==null) {
            Admin admin = new Admin();
            admin.setName("nishant");
            admin.setPassword(passwordEncoder.encode("nishant"));
            admin.setUserType(UserType.Admin);
            admin.setUsername("nishant@gmail.com");
            admin.setAuthorities("Admin");
            adminRepository.save(admin);
            System.out.println("Default admin user created.");
        } else {
            System.out.println("Default admin user already exists.");
        }
    }
}