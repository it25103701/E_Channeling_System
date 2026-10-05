package com.medibook.appointment.service;

import com.medibook.appointment.dto.SlotRequest;
import com.medibook.appointment.dto.SlotResponse;
import com.medibook.appointment.entity.*;
import com.medibook.appointment.repository.AppointmentRepository;
import com.medibook.appointment.repository.DoctorRepository;
import com.medibook.appointment.repository.SlotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlotService {
    private final SlotRepository slots;
    private final DoctorRepository doctors;
    private final AppointmentRepository appointments;

    public SlotService(SlotRepository slots, DoctorRepository doctors, AppointmentRepository appointments) {
        this.slots = slots;
        this.doctors = doctors;
        this.appointments = appointments;
    }

    @Transactional(readOnly = true)
    public List<SlotResponse> available(Long doctorId, LocalDate from, LocalDate to, LocalDate date) {
        List<Slot> result;
        if (date != null) {
            result = doctorId == null ? slots.findByDateAndStatusOrderByTimeAsc(date, SlotStatus.AVAILABLE)
                    : slots.findByDoctorIdAndDateBetweenAndStatusOrderByDateAscTimeAsc(doctorId, date, date, SlotStatus.AVAILABLE);
        } else {
            if (doctorId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "doctorId or date is required");
            result = slots.findByDoctorIdAndDateBetweenAndStatusOrderByDateAscTimeAsc(doctorId,
                    from == null ? LocalDate.now() : from, to == null ? LocalDate.now().plusMonths(1) : to, SlotStatus.AVAILABLE);
        }
        LocalDateTime now = LocalDateTime.now();
        return result.stream()
            .filter(slot -> !slot.getDate().atTime(slot.getTime()).isBefore(now))
            .map(SlotResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public SlotResponse get(Long id) { return SlotResponse.from(find(id)); }

    @Transactional
    public SlotResponse create(SlotRequest request) {
        validateFuture(request.date(), request.time());
        Doctor doctor = doctors.findById(request.doctorId()).orElseThrow(() -> missing("Doctor not found"));
        if (slots.existsByDoctorIdAndDateAndTime(request.doctorId(), request.date(), request.time())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A slot already exists for this doctor, date, and time");
        }
        return SlotResponse.from(slots.save(new Slot(doctor, request.date(), request.time())));
    }

    @Transactional
    public SlotResponse update(Long id, SlotRequest request) {
        Slot slot = find(id);
        if (slot.getStatus() == SlotStatus.BOOKED || appointments.existsBySlotIdAndStatusIn(id, List.of(AppointmentStatus.CONFIRMED))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A booked slot cannot be changed");
        }
        validateFuture(request.date(), request.time());
        Doctor doctor = doctors.findById(request.doctorId()).orElseThrow(() -> missing("Doctor not found"));
        slot.setDoctor(doctor);
        slot.setDate(request.date());
        slot.setTime(request.time());
        return SlotResponse.from(slot);
    }

    @Transactional
    public void delete(Long id) {
        Slot slot = find(id);
        if (slot.getStatus() == SlotStatus.BOOKED || appointments.existsBySlotIdAndStatusIn(id, List.of(AppointmentStatus.CONFIRMED))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A booked slot cannot be deleted");
        }
        slots.delete(slot);
    }

    private Slot find(Long id) { return slots.findById(id).orElseThrow(() -> missing("Slot not found")); }
    private ResponseStatusException missing(String message) { return new ResponseStatusException(HttpStatus.NOT_FOUND, message); }
    private void validateFuture(LocalDate date, java.time.LocalTime time) {
        if (date.atTime(time).isBefore(LocalDateTime.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A slot cannot be in the past");
    }
}
