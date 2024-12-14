package com.example.appointmentservice.Repository;

import com.example.appointmentservice.Model.Appointment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, ObjectId> {
    List<Appointment> findByUserId(String username);

    Appointment findByAppointmentId(String appointmentId);
}
