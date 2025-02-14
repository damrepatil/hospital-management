package com.hospital.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.hospital.entity.Appointment;


public interface Repository extends JpaRepository<Appointment , Integer>{

    boolean existsByDateAndTime(LocalDate date, String time);
    List<Appointment> findByDate(LocalDate date);
    
    
    
}
