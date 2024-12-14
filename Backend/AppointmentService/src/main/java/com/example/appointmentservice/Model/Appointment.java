package com.example.appointmentservice.Model;

import com.example.appointmentservice.DTO.Doctor;
import com.example.appointmentservice.DTO.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {
    @Id
    ObjectId id;
    String appointmentId;
    String userId;
    String docId;
    String slotDate;
    String slotTime;
    User userData;
    Doctor docData;
    int amount;
    Date date;
    boolean cancelled;
    boolean payment;
    boolean isCompleted;
}
