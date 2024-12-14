package com.example.doctorservice.Repository;

import com.example.doctorservice.Model.Doctor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, ObjectId> {
    Doctor findByEmail(String username);
}

