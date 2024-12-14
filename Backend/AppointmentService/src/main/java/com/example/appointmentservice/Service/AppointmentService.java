package com.example.appointmentservice.Service;

import com.example.appointmentservice.Client.DoctorServiceClient;
import com.example.appointmentservice.Client.UserServiceClient;
import com.example.appointmentservice.DTO.AppointmentDTO;
import com.example.appointmentservice.DTO.Doctor;
import com.example.appointmentservice.DTO.User;
import com.example.appointmentservice.Mapper.AppointmentMapper;
import com.example.appointmentservice.Model.Appointment;
import com.example.appointmentservice.Model.DashboardData;
import com.example.appointmentservice.Repository.AppointmentRepository;
import com.example.appointmentservice.Utils.RazorPayUtil;
import com.razorpay.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AppointmentService {
    @Value("${rzp_key_id}")
    private String keyId;

    @Value("${rzp_key_secret}")
    private String secret;

    private final AppointmentRepository appointmentRepository;
    private final DoctorServiceClient doctorServiceClient;
    private final UserServiceClient userServiceClient;
    private final RazorPayUtil razorPayUtil;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorServiceClient doctorServiceClient, UserServiceClient userServiceClient, RazorPayUtil razorPayUtil) {
        this.appointmentRepository = appointmentRepository;
        this.doctorServiceClient = doctorServiceClient;
        this.userServiceClient = userServiceClient;
        this.razorPayUtil = razorPayUtil;
    }

    public Appointment bookAppointment(AppointmentDTO appointmentDTO) {
        try {
            Doctor docData = doctorServiceClient.getDoctor(appointmentDTO.getDocId()).getBody();
            if (docData == null) {
                throw new RuntimeException("Doctor data not found");
            }
            String requestedSlot = appointmentDTO.getUserId() + " Date:" + appointmentDTO.getSlotDate()+" Time: "+appointmentDTO.getSlotTime() ;
            List<String> slots = docData.getSlots() != null ? docData.getSlots() : new ArrayList<>();
            if(!slots.isEmpty()){
                if(slots.contains(requestedSlot)){
                    throw new IllegalArgumentException("Slot already booked");
                }
            }
            slots.add(requestedSlot);
            User user = userServiceClient.getUserData(appointmentDTO.getUserId()).getBody();
            Appointment appointment = AppointmentMapper.mapToAppointment(appointmentDTO);
            appointment.setAppointmentId(UUID.randomUUID().toString());
            appointment.setUserData(user);
            appointment.setDocData(docData);
            appointment.setUserId(user.getEmail());
            appointment.setAmount(docData.getFees());
            appointment.setDate(new Date());
            docData.setSlots(slots);
            doctorServiceClient.bookingSlots(docData);
            return appointmentRepository.save(appointment);
        } catch (IllegalArgumentException e) {
            log.error("Invalid input while booking appointment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error booking appointment", e);
            throw new RuntimeException("Failed to book appointment", e);
        }
    }

    public List<Appointment> getAppointments(String username) {
        List<Appointment> appointments = appointmentRepository.findByUserId(username);
        if(appointments.isEmpty()){
            log.info("no appointment found for {}",username);
        }
        return appointments;
    }

    public boolean cancelAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);
        if (appointment == null) {
            log.warn("No appointment found for id: {}", appointmentId);
            return false;
        }
        appointment.setCancelled(true);
        appointmentRepository.save(appointment);
        return true;
    }

    public String Payment(String appointmentId, RazorpayClient razorpayClient) throws RazorpayException {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);
        if(appointment == null){
            log.warn("No appointment found for id: {}", appointmentId);
            return null;
        }
        int amount = appointment.getAmount()*100;
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_receipt_11");
        Order order = razorpayClient.orders.create(orderRequest);
        appointment.setPayment(true);
        appointmentRepository.save(appointment);
        return order.toString();
    }

    public boolean verifyPayment(String orderId) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, secret);
        List<Payment> payments = razorpayClient.orders.fetchPayments(orderId);
        return !payments.isEmpty();
    }

    public List<Appointment> getAllAppointment() {
        return appointmentRepository.findAll();
    }
    public DashboardData getDashData(DoctorServiceClient doctorServiceClient, UserServiceClient userServiceClient) {
        return DashboardData.builder()
                .doctorsData(doctorServiceClient.getDoctorsData().getBody())
                .userData(userServiceClient.getUserData().getBody())
                .appointmentData(getAllAppointment())
                .build();
    }
}
