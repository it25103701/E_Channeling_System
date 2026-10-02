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

        addDoctor(
                1001,
                "Dr. Silva",
                "Cardiologist",
                "Available"
        );

        addDoctor(
                1002,
                "Dr. Perera",
                "Cardiologist",
                "Available"
        );

        addDoctor(
                1003,
                "Dr. Fernando",
                "Dermatologist",
                "Available"
        );

        addDoctor(
                1004,
                "Dr. Nimal",
                "Cardiologist",
                "Available"
        );

        addDoctor(
                1005,
                "Dr. Ahamed",
                "Neurologist",
                "Available"
        );

        addDoctor(
                1006,
                "Dr. Fathima",
                "Neurologist",
                "Available"
        );

        addDoctor(
                1007,
                "Dr. Kumara",
                "Pediatrician",
                "Available"
        );

        addDoctor(
                1008,
                "Dr. Shalini",
                "Pediatrician",
                "Available"
        );

        addDoctor(
                1009,
                "Dr. Rizwan",
                "Orthopedic",
                "Available"
        );

        addDoctor(
                1010,
                "Dr. Tharushi",
                "Orthopedic",
                "Available"
        );

        addDoctor(
                1011,
                "Dr. Imran",
                "ENT Specialist",
                "Available"
        );

        addDoctor(
                1012,
                "Dr. Nadeesha",
                "ENT Specialist",
                "Available"
        );

        addDoctor(
                1013,
                "Dr. Hashan",
                "Dermatologist",
                "Available"
        );

        addDoctor(
                1014,
                "Dr. Rizana",
                "General Physician",
                "Available"
        );

        addDoctor(
                1015,
                "Dr. Sameera",
                "General Physician",
                "Available"
        );
    }


    private void addDoctor(
            int doctorId,
            String doctorName,
            String specialization,
            String availabilityStatus) {

        if (!doctorRepository.existsById(doctorId)) {

            Doctor doctor =
                    new Doctor(
                            doctorId,
                            doctorName,
                            specialization,
                            availabilityStatus
                    );

            doctorRepository.save(doctor);
        }
    }
}