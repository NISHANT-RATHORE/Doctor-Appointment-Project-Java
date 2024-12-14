package com.example.appointmentservice.Mapper;

import com.example.appointmentservice.DTO.AppointmentDTO;
import com.example.appointmentservice.Model.Appointment;
import lombok.experimental.UtilityClass;


@UtilityClass
public class AppointmentMapper {
    public Appointment mapToAppointment(AppointmentDTO request){
        return Appointment.builder()
                .docId(request.getDocId())
                .slotTime(request.getSlotTime())
                .slotDate(request.getSlotDate())
                .build();
    }
}
