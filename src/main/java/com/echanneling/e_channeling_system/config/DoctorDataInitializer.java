package com.echanneling.e_channeling_system.config;

import com.echanneling.e_channeling_system.dao.DoctorRepository;
import com.echanneling.e_channeling_system.model.Doctor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DoctorDataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    public DoctorDataInitializer(
            DoctorRepository doctorRepository) {

        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {

        // Doctor 1001 - Cardiologist
        if (!doctorRepository.existsById(1001)) {

            Doctor doctor1 = new Doctor(
                    1001,
                    "Dr. Silva",
                    "Cardiologist",
                    "Available"
            );

            doctorRepository.save(doctor1);
        }


        // Doctor 1002 - Cardiologist
        // Can replace Doctor 1001
        if (!doctorRepository.existsById(1002)) {

            Doctor doctor2 = new Doctor(
                    1002,
                    "Dr. Perera",
                    "Cardiologist",
                    "Available"
            );

            doctorRepository.save(doctor2);
        }


        // Doctor 1003 - Dermatologist
        if (!doctorRepository.existsById(1003)) {

            Doctor doctor3 = new Doctor(
                    1003,
                    "Dr. Fernando",
                    "Dermatologist",
                    "Available"
            );

            doctorRepository.save(doctor3);
        }


        // Doctor 1004 - Cardiologist
        // Another possible replacement
        if (!doctorRepository.existsById(1004)) {

            Doctor doctor4 = new Doctor(
                    1004,
                    "Dr. Nimal",
                    "Cardiologist",
                    "Available"
            );

            doctorRepository.save(doctor4);
        }
    }
}