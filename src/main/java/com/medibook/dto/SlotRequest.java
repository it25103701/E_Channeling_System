package com.medibook.appointment.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record SlotRequest(@NotNull Long doctorId, @NotNull LocalDate date, @NotNull LocalTime time) { }
