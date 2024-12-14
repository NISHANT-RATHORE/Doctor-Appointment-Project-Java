package com.example.doctorservice.Repository;

import com.example.doctorservice.Model.Admin;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends MongoRepository<Admin, ObjectId> {

    Admin findByUsername(String email);
}