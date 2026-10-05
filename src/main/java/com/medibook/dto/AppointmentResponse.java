package com.medibook.appointment.dto;
import com.medibook.appointment.entity.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
public record AppointmentResponse(Long id, String patientId, String patientName, String phone, String email, Long doctorId, Long slotId, String doctorName, String specialisation, LocalDate date, LocalTime time, String bookingReference, String appointmentType, String notes, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(a.getId(), a.getPatientId(), a.getPatientName(), a.getPatientPhone(), a.getPatientEmail(), a.getDoctor().getId(), a.getSlot().getId(), a.getDoctor().getName(), a.getDoctor().getSpecialisation(), a.getSlot().getDate(), a.getSlot().getTime(), a.getBookingReference(), a.getAppointmentType(), a.getNotes(), a.getStatus().name(), a.getCreatedAt(), a.getUpdatedAt());
    }
}