package com.echanneling.e_channeling_system.dto;

import jakarta.validation.constraints.NotNull;

public class RescheduleRequest {
    @NotNull
    private Long newSlotId;
    private String appointmentType;
    private String notes;

    public Long getNewSlotId() { return newSlotId; }
    public String getAppointmentType() { return appointmentType; }
    public String getNotes() { return notes; }
}