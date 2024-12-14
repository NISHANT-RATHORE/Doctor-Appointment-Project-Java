package com.example.appointmentservice.Controller;

import com.example.appointmentservice.Client.DoctorServiceClient;
import com.example.appointmentservice.Client.UserServiceClient;
import com.example.appointmentservice.DTO.AppointmentDTO;
import com.example.appointmentservice.DTO.Doctor;
import com.example.appointmentservice.DTO.User;
import com.example.appointmentservice.Model.Appointment;
import com.example.appointmentservice.Model.DashboardData;
import com.example.appointmentservice.Service.AppointmentService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = {"${frontend.url1}", "${frontend.url2}"})
public class AppointmentController {

    @Value("${rzp_key_id}")
    private String keyId;

    @Value("${rzp_key_secret}")
    private String secret;

    private final DoctorServiceClient doctorServiceClient;
    private final UserServiceClient userServiceClient;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(DoctorServiceClient doctorServiceClient, UserServiceClient userServiceClient, AppointmentService appointmentService) {
        this.doctorServiceClient = doctorServiceClient;
        this.userServiceClient = userServiceClient;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/getDoctor")
    public ResponseEntity<Doctor> getDoctor(@RequestParam String username){
        try{
            return doctorServiceClient.getDoctor(username);
        } catch (Exception e){
            log.error("Error retrieving doctor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAllDoctorData")
    public ResponseEntity<List<Doctor>> getDoctorsData(){
        try{
            return doctorServiceClient.getDoctorsData();
        } catch (Exception e){
            log.error("Error retrieving doctor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam String username){
        try{
            return userServiceClient.getUserData(username);
        } catch (Exception e){
            log.error("Error retrieving doctor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAllUserData")
    public ResponseEntity<List<User>> getUserData(){
        try{
            return userServiceClient.getUserData();
        } catch (Exception e){
            log.error("Error retrieving doctor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/bookAppointment")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        try {
            log.info("Booking Appointment initiated....");
            Appointment appointment = appointmentService.bookAppointment(appointmentDTO);
            log.info("Appointment booked successfully");
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            log.error("Error booking appointment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAppointments")
    public ResponseEntity<List<Appointment>> getAppointments(@RequestParam String username) {
        try {
            log.info("Retrieving appointments for user: {}", username);
            List<Appointment> appointments = appointmentService.getAppointments(username);
            log.info("Appointments retrieved successfully");
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            log.error("Error retrieving appointments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/cancelAppointment")
    public ResponseEntity<String> cancelAppointment(@RequestParam String appointmentId) {
        try {
            log.info("Cancelling appointment: {}", appointmentId);
            boolean isCancelled = appointmentService.cancelAppointment(appointmentId);
            if (!isCancelled) {
                log.warn("Appointment not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            log.info("Appointment cancelled successfully");
            return ResponseEntity.ok("Appointment cancelled successfully");
        } catch (Exception e) {
            log.error("Error cancelling appointment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/payment")
    public ResponseEntity<String> Payment(@RequestParam String appointmentId) throws RazorpayException {
        try {
            log.info("Payment initiated....");
            RazorpayClient razorpayClient = new RazorpayClient(keyId, secret);
            String order = appointmentService.Payment(appointmentId, razorpayClient);
            log.info("Payment successful");
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.error("Error processing payment", e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error processing payment");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment");
        }
    }

    @PostMapping("/verifyPayment")
    public ResponseEntity<String> verifyPayment(@RequestParam(name = "razorpay_order_id") String orderId) {
        try {
            boolean isValid = appointmentService.verifyPayment(orderId);
            if (isValid) {
                return ResponseEntity.ok("Payment verified successfully");
            } else {
                return ResponseEntity.status(400).body("Payment verification failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error verifying payment");
        }
    }

    @GetMapping("/allAppointments")
    public ResponseEntity<List<Appointment>> getAllAppointment(){
        try{
            log.info("Retriving all Appointments.....");
            List<Appointment> getAllAppointment = appointmentService.getAllAppointment();
            if(getAllAppointment.isEmpty()){
                log.warn("no appointment found.");
                return ResponseEntity.status(200).build();
            }
            log.info("Successfully fetched the appointment");
            return ResponseEntity.ok(getAllAppointment);
        } catch (Exception e){
            log.error("Error in found the Appointment");
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/DashData")
    public ResponseEntity<DashboardData> getAll(){
        try {
            DashboardData dashboardData = appointmentService.getDashData(doctorServiceClient,userServiceClient);
            if (dashboardData.equals(null)) {
                log.warn("No data found for dashboard");
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.info("successfully fetched dashboard data");
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            log.error("Error retrieving doctors", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
