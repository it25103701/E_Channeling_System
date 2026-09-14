package com.echanneling.e_channeling_system.dao;

import com.echanneling.e_channeling_system.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Integer> {
}