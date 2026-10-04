package com.echanneling.e_channeling_system.dao;

import com.echanneling.e_channeling_system.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findBySpecializationIgnoreCaseAndAvailabilityStatusIgnoreCaseAndDoctorIdNot(
            String specialization,
            String availabilityStatus,
            int doctorId
    );
}