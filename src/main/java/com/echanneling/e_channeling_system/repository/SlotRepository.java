package com.echanneling.e_channeling_system.repository;

import com.echanneling.e_channeling_system.entity.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s join fetch s.doctor where s.id = :id")
    Optional<Slot> findByIdForUpdate(@Param("id") Long id);

    List<Slot> findByDoctorIdAndDateBetweenAndStatusOrderByDateAscTimeAsc(Long doctorId, LocalDate from, LocalDate to, SlotStatus status);

    List<Slot> findByDateAndStatusOrderByTimeAsc(LocalDate date, SlotStatus status);
}