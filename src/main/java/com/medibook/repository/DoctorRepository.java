package com.medibook.appointment.repository;
import com.medibook.appointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialisationContainingIgnoreCase(String name, String specialisation);
}