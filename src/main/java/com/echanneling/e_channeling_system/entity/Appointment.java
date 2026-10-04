package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appointment_patient", columnList = "patient_id"),
        @Index(name = "idx_appointment_date_status", columnList = "slot_id,status"),
        @Index(name = "idx_appointment_reference", columnList = "booking_reference")
})
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String patientId;
    @Column(nullable = false) private String patientName;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "doctor_id") private Doctor doctor;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "slot_id") private Slot slot;
    @Column(nullable = false, unique = true) private String bookingReference;
    @Column(nullable = false) private String appointmentType;
    @Column(length = 1000) private String notes;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private AppointmentStatus status;
    @Column(nullable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;

    public Appointment() { }

    public Appointment(String patientId, String patientName, Doctor doctor, Slot slot, String reference, String appointmentType, String notes) {
        this.patientId = patientId; this.patientName = patientName; this.doctor = doctor; this.slot = slot;
        this.bookingReference = reference; this.appointmentType = appointmentType; this.notes = notes;
        this.status = AppointmentStatus.CONFIRMED; this.createdAt = LocalDateTime.now(); this.updatedAt = this.createdAt;
    }

    public Long getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public Doctor getDoctor() { return doctor; }
    public Slot getSlot() { return slot; }
    public String getBookingReference() { return bookingReference; }
    public String getAppointmentType() { return appointmentType; }
    public String getNotes() { return notes; }
    public AppointmentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setSlot(Slot slot) { this.slot = slot; this.updatedAt = LocalDateTime.now(); }
    public void setStatus(AppointmentStatus status) { this.status = status; this.updatedAt = LocalDateTime.now(); }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; this.updatedAt = LocalDateTime.now(); }
    public void setNotes(String notes) { this.notes = notes; this.updatedAt = LocalDateTime.now(); }

    @PrePersist
    private void setCreationTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    private void updateTimestamp() { updatedAt = LocalDateTime.now(); }
}