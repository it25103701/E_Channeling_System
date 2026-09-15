package com.echanneling.e_channeling_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAppointmentRequest {
    private String patientId;
    @NotBlank
    private String patientName;
    @NotNull
    private Long slotId;
    private String appointmentType;
    private String notes;

    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public Long getSlotId() { return slotId; }
    public String getAppointmentType() { return appointmentType; }
    public String getNotes() { return notes; }
}