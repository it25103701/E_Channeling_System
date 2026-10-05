package com.medibook.appointment.repository;
import com.medibook.appointment.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("select a from Appointment a join fetch a.doctor join fetch a.slot where a.patientId = :patientId order by a.slot.date asc, a.slot.time asc")
    List<Appointment> findByPatient(@Param("patientId") String patientId);
    @Query("select a from Appointment a join fetch a.doctor join fetch a.slot where a.patientId = :patientId order by a.slot.date asc, a.slot.time asc")
    List<Appointment> findByPatientWithDetails(@Param("patientId") String patientId);
    @Query("select a from Appointment a join fetch a.doctor join fetch a.slot where a.slot.date = :date and a.doctor.id = :doctorId order by a.slot.time asc")
    List<Appointment> findByDateAndDoctor(@Param("date") LocalDate date, @Param("doctorId") Long doctorId);
    @Query("select a from Appointment a join fetch a.doctor join fetch a.slot where a.slot.date = :date order by a.slot.time asc")
    List<Appointment> findByDate(@Param("date") LocalDate date);
    @Query("select a from Appointment a join fetch a.doctor join fetch a.slot where a.patientId = :patientId and lower(a.bookingReference) like lower(concat('%', :reference, '%')) and (:date is null or a.slot.date = :date) and (:status is null or a.status = :status) order by a.slot.date asc, a.slot.time asc")
    List<Appointment> search(@Param("patientId") String patientId, @Param("reference") String reference, @Param("date") LocalDate date, @Param("status") AppointmentStatus status);
    boolean existsBySlotIdAndStatusIn(Long slotId, java.util.Collection<AppointmentStatus> statuses);
    boolean existsByDoctorId(Long doctorId);
}
