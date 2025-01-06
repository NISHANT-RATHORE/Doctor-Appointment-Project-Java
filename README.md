# Doctor Appointment Booking System

![doctor-appointment](https://github.com/user-attachments/assets/5ad2140b-f82a-4b48-adf2-4775fbac211b)

## Overview

The **Doctor Appointment Booking System** is a full-stack web application built to streamline the process of booking doctor appointments. It features a modern frontend using **Vite React** and **Tailwind CSS**, and a robust backend with a **microservice-based architecture** powered by **Spring Boot**.

## Features

### Frontend
- **Responsive Design**: Built with Tailwind CSS for a clean and user-friendly interface.
- **User Authentication**: JWT-based authentication and authorization for secure access.
- **Dynamic Data Rendering**: Patients can browse and book appointments seamlessly.
- **Payment Integration**: Razorpay integrated for secure fee transactions.

### Backend Microservices
1. **Service Registry**:
   - Centralized service registration and discovery for all microservices.

2. **UserService**:
   - Manages patient registration and login using JWT authentication.
   - Supports profile updates, including uploading profile pictures via **Cloudinary**.
   - Stores user details like address, phone number, and date of birth.

3. **DoctorService**:
   - Lists all available doctors with specializations and availability.

4. **AppointmentService**:
   - Allows patients to book appointments based on preferred dates and times.
   - Verifies fee payment through Razorpay before confirming the booking.

### Database and Hosting
- **MongoDB Atlas**: Used for storing user, doctor, and appointment data.
- **Cloudinary**: Used for managing and storing user profile images.
- **Deployment**: Microservices deployed on AWS ec2 instances and frontend deployed on vercel.

## Tech Stack

### Frontend
- **Vite React**: For fast and modern development.
- **Tailwind CSS**: For a responsive and stylish UI.
- **Libraries**: React Router, Axios, and others for advanced functionalities.

### Backend
- **Spring Boot**: For creating microservices.
- **MongoDB**: As a NoSQL database for storing data.
- **Razorpay**: For secure and efficient payment processing.
- **JWT**: For handling authentication and authorization.

## Core Functionalities
1. **Registration and Login**:
   - Secure patient authentication with JWT.
   - Registration includes uploading profile pictures and saving user details.

2. **Browse Doctors**:
   - Patients can view doctor profiles with their specialization and availability.

3. **Book Appointments**:
   - Patients can select a doctor, choose a date and time, and complete payments via Razorpay.

4. **Profile Management**:
   - Users can update their profile details, including address, phone number, and more.

## Future Enhancements
- Adding role-based access for doctors and admins.
- Sending email/SMS notifications for appointment reminders.
- Integrating AI-based doctor recommendations.

## Contributing
Contributions are welcome! Fork the repository, make your changes, and submit a pull request.
