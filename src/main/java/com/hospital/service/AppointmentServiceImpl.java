package com.hospital.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.entity.Appointment;
import com.hospital.repo.Repository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private Repository repository;

    public void addAppointment(Appointment appointment)
    {
        this.repository.save(appointment);

    }

    @Override
    public boolean isSlotBooked(LocalDate date, String time) {
        return repository.existsByDateAndTime(date, time);
    }

    @Override
   public List<String> getBookedSlotsForDate(LocalDate date) {
        return repository.findByDate(date)
                .stream()
                .map(Appointment::getTime)
                .collect(Collectors.toList());
    }

    
}
