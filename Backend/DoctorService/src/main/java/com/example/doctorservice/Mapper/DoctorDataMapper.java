package com.example.doctorservice.Mapper;

import com.example.doctorservice.DTO.DoctorDataRequest;
import com.example.doctorservice.Model.Doctor;
import com.example.doctorservice.Repository.DoctorRepository;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

@UtilityClass
public class DoctorDataMapper {
    public static DoctorDataRequest mapToDoctorDataRequest(Doctor doctor) {
        return DoctorDataRequest.builder()
                .name(doctor.getName())
                .email(doctor.getEmail())
                .speciality(doctor.getSpeciality())
                .degree(doctor.getDegree())
                .experience(doctor.getExperience())
                .about(doctor.getAbout())
                .fees(doctor.getFees())
                .address1(doctor.getAddress1())
                .address2(doctor.getAddress2())
                .docImg(doctor.getDocImg())
                .build();
    }
}
