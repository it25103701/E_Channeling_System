package com.echanneling.e_channeling_system.repository;

import com.echanneling.e_channeling_system.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Integer> {
}