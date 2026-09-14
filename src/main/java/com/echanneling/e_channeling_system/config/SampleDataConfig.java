package com.echanneling.e_channeling_system.config;

import com.echanneling.e_channeling_system.entity.Doctor;
import com.echanneling.e_channeling_system.entity.Slot;
import com.echanneling.e_channeling_system.repository.DoctorRepository;
import com.echanneling.e_channeling_system.repository.SlotRepository;
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
            if (doctors.count() > 0) return;
            Doctor fernando = doctors.save(new Doctor("Dr. Anjali Fernando", "Cardiology", "MBBS, MD", "MediBook Central Clinic"));
            Doctor perera = doctors.save(new Doctor("Dr. Kavindu Perera", "Dermatology", "MBBS, D.Derm", "MediBook Central Clinic"));
            Doctor silva = doctors.save(new Doctor("Dr. Nethmi Silva", "General Medicine", "MBBS, MSc", "MediBook City Clinic"));
            LocalDate today = LocalDate.now();
            for (Doctor doctor : List.of(fernando, perera, silva)) {
                for (int day = 0; day < 7; day++) {
                    slots.save(new Slot(doctor, today.plusDays(day), LocalTime.of(9, 0)));
                    slots.save(new Slot(doctor, today.plusDays(day), LocalTime.of(10, 0)));
                    slots.save(new Slot(doctor, today.plusDays(day), LocalTime.of(11, 30)));
                }
            }
        };
    }
}
