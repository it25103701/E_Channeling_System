package com.medibook.appointment.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record CreateAppointmentRequest(@NotBlank String patientId, @NotBlank String patientName, String phone, String email, @NotNull Long slotId, @NotBlank String appointmentType, String notes) { }