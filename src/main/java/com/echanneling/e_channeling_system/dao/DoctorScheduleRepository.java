package com.echanneling.e_channeling_system.dao;

import com.echanneling.e_channeling_system.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Integer> {

    @Query("""
            SELECT COUNT(s)
            FROM DoctorSchedule s
            WHERE s.doctorId = :doctorId
            AND s.consultationDate = :consultationDate
            AND s.status <> 'Cancelled'
            AND s.startTime < :endTime
            AND s.endTime > :startTime
            AND (:scheduleId = 0 OR s.scheduleId <> :scheduleId)
            """)
    long countOverlappingSchedules(
            @Param("doctorId") int doctorId,
            @Param("consultationDate") LocalDate consultationDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("scheduleId") int scheduleId
    );
}