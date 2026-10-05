package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.dto.AppointmentResponse;
import com.echanneling.e_channeling_system.dto.CreateAppointmentRequest;
import com.echanneling.e_channeling_system.dto.RescheduleRequest;
import com.echanneling.e_channeling_system.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing clinical appointments (UC-01).
 * Provides endpoints for searching, retrieving, booking, rescheduling, and cancelling appointments.
 */
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    /**
     * Retrieves appointments filtered by patient, booking reference, consultation date, status, or doctor.
     * Delegates to daily schedule retrieval when only date filtering is applied.
     *
     * @param patientId ID of the patient (defaults to 'demo-patient' if omitted)
     * @param reference Unique booking reference
     * @param date      Consultation date in ISO format (yyyy-MM-dd)
     * @param status    Appointment status
     * @param doctorId  Target doctor identifier
     * @return List of matching appointment records
     */
    @GetMapping
    public List<AppointmentResponse> list(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long doctorId) {

        if (date != null && reference == null && status == null) {
            return service.daily(date, doctorId);
        }

        String effectivePatientId = (patientId == null) ? "demo-patient" : patientId;
        return service.search(effectivePatientId, reference, date, status);
    }

    /**
     * Fetches details of a specific appointment by primary identifier.
     *
     * @param id Appointment primary key
     * @return Corresponding appointment details
     */
    @GetMapping("/{id}")
    public AppointmentResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    /**
     * Books a new patient appointment slot.
     *
     * @param request Validated payload containing patient, doctor, and slot parameters
     * @return Created appointment confirmation
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody CreateAppointmentRequest request) {
        return service.create(request);
    }

    /**
     * Reschedules an existing confirmed appointment to a new date/slot.
     *
     * @param id      Target appointment identifier
     * @param request Validated payload containing updated slot timing
     * @return Updated appointment record
     */
    @PutMapping("/{id}/reschedule")
    public AppointmentResponse reschedule(
            @PathVariable Long id,
            @Valid @RequestBody RescheduleRequest request) {
        return service.reschedule(id, request);
    }

    /**
     * Cancels an existing appointment.
     *
     * @param id Target appointment identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        service.cancel(id);
    }
}