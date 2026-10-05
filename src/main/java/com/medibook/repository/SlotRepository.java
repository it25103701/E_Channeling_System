package com.medibook.appointment.repository;
import com.medibook.appointment.entity.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s join fetch s.doctor where s.id = :id") Optional<Slot> findByIdForUpdate(@Param("id") Long id);
    List<Slot> findByDoctorIdAndDateBetweenAndStatusOrderByDateAscTimeAsc(Long doctorId, LocalDate from, LocalDate to, SlotStatus status);
    List<Slot> findByDateAndStatusOrderByTimeAsc(LocalDate date, SlotStatus status);
    List<Slot> findByDoctorIdAndDateBetweenOrderByDateAscTimeAsc(Long doctorId, LocalDate from, LocalDate to);
    List<Slot> findByDateOrderByTimeAsc(LocalDate date);
    boolean existsByDoctorId(Long doctorId);
    boolean existsByDoctorIdAndDateAndTime(Long doctorId, LocalDate date, java.time.LocalTime time);
}
