package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.dto.*;
import com.echanneling.e_channeling_system.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppointmentResponse> list(@RequestParam(required = false) String patientId,
                                          @RequestParam(required = false) String reference,
                                          @RequestParam(required = false) LocalDate date,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) Long doctorId) {
        if (date != null && reference == null && status == null)
            return service.daily(date, doctorId);
        return service.search(patientId == null ? "demo-patient" : patientId, reference, date, status);
    }

    @GetMapping("/{id}")
    public AppointmentResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody CreateAppointmentRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}/reschedule")
    public AppointmentResponse reschedule(@PathVariable Long id, @Valid @RequestBody RescheduleRequest request) {
        return service.reschedule(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        service.cancel(id);
    }
}