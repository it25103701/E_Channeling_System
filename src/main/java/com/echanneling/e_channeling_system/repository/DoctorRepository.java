package com.echanneling.e_channeling_system.repository;

import com.echanneling.e_channeling_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByNameContainingIgnoreCaseAndSpecialisationContainingIgnoreCase(
            String name,
            String specialisation
    );

    List<Doctor> findBySpecialisationIgnoreCaseAndIdNot(
            String specialisation,
            Long id
    );
}