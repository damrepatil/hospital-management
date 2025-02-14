package com.hospital.service;

import java.time.LocalDate;
import java.util.List;

import com.hospital.entity.Appointment;

    
    public interface AppointmentService {
    void addAppointment(Appointment appointment);

    boolean isSlotBooked(LocalDate date, String time);
    
    List<String> getBookedSlotsForDate(LocalDate date);
}
    

