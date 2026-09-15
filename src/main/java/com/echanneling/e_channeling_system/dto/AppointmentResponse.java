package com.echanneling.e_channeling_system.dto;

import com.echanneling.e_channeling_system.entity.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentResponse {
    private Long id;
    private String patientId;
    private String patientName;
    private String bookingReference;
    private String appointmentType;
    private String notes;
    private String status;
    private Long doctorId;
    private String doctorName;
    private String specialisation;
    private Long slotId;
    private LocalDate date;
    private LocalTime time;

    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getId();
        this.patientId = appointment.getPatientId();
        this.patientName = appointment.getPatientName();
        this.bookingReference = appointment.getBookingReference();
        this.appointmentType = appointment.getAppointmentType();
        this.notes = appointment.getNotes();
        this.status = appointment.getStatus().name();
        if (appointment.getDoctor() != null) {
            this.doctorId = appointment.getDoctor().getId();
            this.doctorName = appointment.getDoctor().getName();
            this.specialisation = appointment.getDoctor().getSpecialisation();
        }
        if (appointment.getSlot() != null) {
            this.slotId = appointment.getSlot().getId();
            this.date = appointment.getSlot().getDate();
            this.time = appointment.getSlot().getTime();
        }
    }

    public Long getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getBookingReference() { return bookingReference; }
    public String getAppointmentType() { return appointmentType; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialisation() { return specialisation; }
    public Long getSlotId() { return slotId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
}