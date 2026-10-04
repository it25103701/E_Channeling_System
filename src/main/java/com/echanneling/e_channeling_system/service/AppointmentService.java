package com.echanneling.e_channeling_system.service;

import com.echanneling.e_channeling_system.dto.*;
import com.echanneling.e_channeling_system.entity.*;
import com.echanneling.e_channeling_system.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, SlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest request) {
        Slot slot = availableSlot(request.getSlotId());
        rejectPast(slot);
        slot.setStatus(SlotStatus.BOOKED);
        Appointment appointment = appointmentRepository.save(new Appointment(
                request.getPatientId(),
                request.getPatientName(),
                slot.getDoctor(),
                slot,
                "ECHN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                request.getAppointmentType(),
                request.getNotes()
        ));
        return new AppointmentResponse(appointment);
    }

    @Transactional(readOnly = true)
    public AppointmentResponse get(Long id) {
        return new AppointmentResponse(appointmentRepository.findById(id).orElseThrow(() -> missing("Appointment not found")));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> byPatient(String patientId) {
        return appointmentRepository.findByPatient(patientId).stream().map(AppointmentResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> search(String patientId, String reference, LocalDate date, String status) {
        AppointmentStatus appointmentStatus = status == null || status.isBlank() ? null : parseStatus(status);
        return appointmentRepository.search(patientId, reference == null ? "" : reference, date, appointmentStatus).stream().map(AppointmentResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> daily(LocalDate date, Long doctorId) {
        var appointments = doctorId == null ? appointmentRepository.findByDate(date) : appointmentRepository.findByDateAndDoctor(date, doctorId);
        return appointments.stream().map(AppointmentResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponse reschedule(Long id, RescheduleRequest request) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> missing("Appointment not found"));
        if (appointment.getStatus() == AppointmentStatus.CANCELLED || appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This appointment cannot be rescheduled");
        }
        if (appointment.getSlot().getId().equals(request.getNewSlotId())) {
            appointment.setAppointmentType(request.getAppointmentType());
            appointment.setNotes(request.getNotes());
            return new AppointmentResponse(appointmentRepository.save(appointment));
        }
        Slot newSlot = availableSlot(request.getNewSlotId());
        rejectPast(newSlot);
        appointment.getSlot().setStatus(SlotStatus.AVAILABLE);
        newSlot.setStatus(SlotStatus.BOOKED);
        appointment.setSlot(newSlot);
        appointment.setAppointmentType(request.getAppointmentType());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);
        return new AppointmentResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public void cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> missing("Appointment not found"));
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Appointment is already cancelled");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Completed appointments cannot be cancelled");
        }
        if (appointment.getSlot().getDate().atTime(appointment.getSlot().getTime()).isBefore(LocalDateTime.now().plusHours(24))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments can only be cancelled at least 24 hours before the consultation");
        }
        appointment.getSlot().setStatus(SlotStatus.AVAILABLE);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private Slot availableSlot(Long id) {
        Slot slot = slotRepository.findByIdForUpdate(id).orElseThrow(() -> missing("Slot not found"));
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This slot is already booked");
        }
        return slot;
    }

    private void rejectPast(Slot slot) {
        if (slot.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments cannot be booked in the past");
        }
    }

    private AppointmentStatus parseStatus(String status) {
        try {
            return AppointmentStatus.valueOf(status.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid appointment status");
        }
    }

    private ResponseStatusException missing(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}