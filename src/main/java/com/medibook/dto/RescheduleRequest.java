package com.medibook.appointment.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
public record RescheduleRequest(@NotNull Long newSlotId, @NotBlank String appointmentType, String notes) { }