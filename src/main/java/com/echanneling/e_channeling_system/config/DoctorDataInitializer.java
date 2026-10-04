package com.echanneling.e_channeling_system.config;

import com.echanneling.e_channeling_system.entity.Doctor;
import com.echanneling.e_channeling_system.repository.DoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DoctorDataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    public DoctorDataInitializer(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {

        if (doctorRepository.count() == 0) {
            addDoctor("Dr. Silva", "Cardiologist", "MBBS, MD", "Room 101");
            addDoctor("Dr. Perera", "Cardiologist", "MBBS, MD", "Room 102");
            addDoctor("Dr. Fernando", "Dermatologist", "MBBS, DDV", "Room 103");
            addDoctor("Dr. Nimal", "Cardiologist", "MBBS, MD", "Room 104");
            addDoctor("Dr. Ahamed", "Neurologist", "MBBS, DM", "Room 105");
            addDoctor("Dr. Fathima", "Neurologist", "MBBS, DM", "Room 106");
            addDoctor("Dr. Kumara", "Pediatrician", "MBBS, DCH", "Room 107");
            addDoctor("Dr. Shalini", "Pediatrician", "MBBS, MD", "Room 108");
            addDoctor("Dr. Rizwan", "Orthopedic", "MBBS, MS", "Room 109");
            addDoctor("Dr. Tharushi", "Orthopedic", "MBBS, MS", "Room 110");
            addDoctor("Dr. Imran", "ENT Specialist", "MBBS, MS", "Room 111");
            addDoctor("Dr. Nadeesha", "ENT Specialist", "MBBS, DLO", "Room 112");
            addDoctor("Dr. Hashan", "Dermatologist", "MBBS, MD", "Room 113");
            addDoctor("Dr. Rizana", "General Physician", "MBBS", "Room 114");
            addDoctor("Dr. Sameera", "General Physician", "MBBS", "Room 115");
        }
    }

    private void addDoctor(
            String doctorName,
            String specialisation,
            String qualification,
            String clinic) {

        Doctor doctor = new Doctor(
                doctorName,
                specialisation,
                qualification,
                clinic
        );
        doctorRepository.save(doctor);
    }
}