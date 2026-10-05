package com.medibook.appointment.dto;

import com.medibook.appointment.entity.Slot;
import java.time.LocalDate;
import java.time.LocalTime;

public record SlotResponse(Long id, Long doctorId, String doctorName, LocalDate date, LocalTime time, String status) {
    public static SlotResponse from(Slot slot) {
        return new SlotResponse(slot.getId(), slot.getDoctor().getId(), slot.getDoctor().getName(),
                slot.getDate(), slot.getTime(), slot.getStatus().name());
    }
}
