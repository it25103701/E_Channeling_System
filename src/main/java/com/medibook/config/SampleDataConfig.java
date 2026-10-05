package com.medibook.appointment.config;

import com.medibook.appointment.entity.*;
import com.medibook.appointment.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class SampleDataConfig {
    @Bean
    CommandLineRunner sampleData(DoctorRepository doctors, SlotRepository slots) {
        return args -> {
            if (doctors.count() == 0) {
                doctors.saveAll(List.of(
                        new Doctor("Dr. Anjali Fernando", "Cardiology", "MBBS, MD", "MediBook Central Clinic"),
                        new Doctor("Dr. Kavindu Perera", "Dermatology", "MBBS, D.Derm", "MediBook Central Clinic"),
                        new Doctor("Dr. Nethmi Silva", "General Medicine", "MBBS, MSc", "MediBook City Clinic")
                    ));
            }
            List<Doctor> seededDoctors = doctors.findAll();
            LocalDate today = LocalDate.now();
            List<Slot> seededSlots = new java.util.ArrayList<>();
            for (Doctor doctor : seededDoctors) {
                for (int day = 0; day < 14; day++) {
                    LocalDate date = today.plusDays(day);
                    for (LocalTime time : List.of(LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 30))) {
                        if (!slots.existsByDoctorIdAndDateAndTime(doctor.getId(), date, time)) {
                            seededSlots.add(new Slot(doctor, date, time));
                        }
                    }
                }
            }
            if (!seededSlots.isEmpty()) slots.saveAll(seededSlots);
        };
    }
}