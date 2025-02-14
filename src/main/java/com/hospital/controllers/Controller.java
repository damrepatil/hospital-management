package com.hospital.controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.entity.Appointment;
import com.hospital.service.AppointmentServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@CrossOrigin(origins = "*") 
@RestController
@RequestMapping("/api/appointments")
public class Controller {

    @Autowired
    private AppointmentServiceImpl appointmentServiceImpl;

    @Autowired
    private JavaMailSender mailSender;

    private final List<String> ALL_SLOTS = Arrays.asList(
        "10:00-11:00", 
        "11:00-12:00",
        "12:00-01:00",
        "02:00-03:00",
        "03:00-04:00",
        "04:00-05:00",
        "05:00-06:00"
    );

    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(@RequestParam LocalDate date) {
        List<String> bookedSlots = appointmentServiceImpl.getBookedSlotsForDate(date);
        System.out.println(bookedSlots);
        List<String> list = ALL_SLOTS.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
                return list;
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam LocalDate date,
                                @RequestParam String time,
                                @RequestParam String gender) 
    {
        if (appointmentServiceImpl.isSlotBooked(date, time)) {
            return ResponseEntity.badRequest().body("This time slot is already booked");
        }

        Appointment appointment =new Appointment();
        appointment.setName(name);
        appointment.setDate(date);
        appointment.setEmail(email);
        appointment.setPhone(phone);
        appointment.setTime(time);
        appointment.setGender(gender);


        appointmentServiceImpl.addAppointment(appointment);

        String hospitalEmail = "vikastesting1122@gmail.com";

         // Preparing email content
    String subject = "Appointment Confirmation - ";
    String body = "Dear " + name + ",\n\n" +
                      "Your appointment has been successfully booked.\n" +
                      "Details:\n" +
                      "Date: " + date + "\n" +
                      "Time: " + time + "\n" +
                      "We look forward to serving you.\n\n" +
                      "Best Regards,\nHospital Team";
    // Sending confirmation email
    sendEmail(email, subject, body);

    // sending to hospital mail
    sendHospitalEmail(hospitalEmail, name, email, phone, date, gender, time);

    return ResponseEntity.ok("Appointment Successfully Registered");
}

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("vikastesting1122@gmail.com"); 
        mailSender.send(message);
    }


    public void sendHospitalEmail(String hospitalEmail, String name, String email, String phone, LocalDate date, String gender, String time) {
        String subject = "New Appointment Booked - " + date;
        String body ="Dear Hospital Team,\n\n" +
                      "A new appointment has been booked.\n\n" +
                      "Patient Details:\n" +
                      "Name: " + name + "\n" +
                      "Email: " + email + "\n" +
                      "Phone: " + phone + "\n" +
                      "Gender: " + gender + "\n" +
                      "Date: " + date + "\n" +
                      "Time: " + time + "\n\n" +
                      "Best Regards,\nHospital System";
    
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(hospitalEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    
    }


