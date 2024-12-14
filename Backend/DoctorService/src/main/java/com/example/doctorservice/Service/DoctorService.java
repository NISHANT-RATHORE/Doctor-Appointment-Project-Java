package com.example.doctorservice.Service;

import com.example.doctorservice.DTO.AddDoctorRequest;
import com.example.doctorservice.DTO.DoctorDataRequest;
import com.example.doctorservice.DTO.ImageModel;
import com.example.doctorservice.Enums.UserType;
import com.example.doctorservice.Mapper.DoctorDataMapper;
import com.example.doctorservice.Mapper.DoctorMapper;
import com.example.doctorservice.Model.Doctor;
import com.example.doctorservice.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService{

    private final CloudinaryService cloudinaryService;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorService(CloudinaryService cloudinaryService, DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.cloudinaryService = cloudinaryService;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor addDoctor(MultipartFile docIm, AddDoctorRequest request) throws IOException {
        if (doctorRepository.findByEmail(request.getEmail()) != null) {
            return null; // Doctor already exists
        }
        ImageModel docImg = ImageModel.builder().file(docIm).build();
        String docImgURL = uploadImage(docImg);
        Doctor doctor = DoctorMapper.mapToDoctor(request);
        doctor.setUserType(UserType.Doctor);
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setAuthorities("Doctor");
        doctor.setDocImg(docImgURL);
        doctor.setDate(new Date());
        doctor.setSlots(new ArrayList<>());
        doctor.setAvailable(true);
        return doctorRepository.save(doctor);
    }


    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public String uploadImage(ImageModel imageModel) {
        try {
            if (imageModel.getFile().isEmpty()) {
                return null;
            }
            String imageUrl = cloudinaryService.uploadFile(imageModel.getFile(), "DoctorPhotos");
            if (imageUrl == null) {
                return null;
            }
            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean changeAvailabilityByEmail(String email) {
        Optional<Doctor> doctorOptional = Optional.ofNullable(doctorRepository.findByEmail(email));
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            doctor.setAvailable(!doctor.getAvailable());
            doctorRepository.save(doctor);
            return true;
        } else {
            return false;
        }
    }

    public DoctorDataRequest getDoctor(String username) {
        Doctor Foundeddoctor =  doctorRepository.findByEmail(username);
        return DoctorDataMapper.mapToDoctorDataRequest(Foundeddoctor);
    }

    public Doctor updateDoctor(DoctorDataRequest request) {
        Doctor doctor = doctorRepository.findByEmail(request.getEmail());
        if (doctor == null) {
            throw new RuntimeException("Doctor not found with email: " + request.getEmail());
        }
        doctor.setSlots(request.getSlots());
        return doctorRepository.save(doctor);
    }

    public List<DoctorDataRequest> getDoctorsData() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorDataRequest> doctorDataRequests = new ArrayList<>();
        for(Doctor doctor : doctors){
            doctorDataRequests.add(DoctorDataMapper.mapToDoctorDataRequest(doctor));
        }
        return doctorDataRequests;
    }
}