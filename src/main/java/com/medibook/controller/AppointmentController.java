package com.medibook.appointment.controller;
import com.medibook.appointment.dto.*;
import com.medibook.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController @RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service = service; }
    @GetMapping public List<AppointmentResponse> list(@RequestParam String patientId, @RequestParam(required = false) String reference, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String status) { return service.search(patientId, reference, date, status); }
    @GetMapping("/{id}") public AppointmentResponse get(@PathVariable Long id) { return service.get(id); }
    @GetMapping("/patient/{patientId}") public List<AppointmentResponse> patientAppointments(@PathVariable String patientId) { return service.byPatient(patientId); }
    @GetMapping("/patient/{patientId}/upcoming") public List<AppointmentResponse> upcoming(@PathVariable String patientId) { return service.upcoming(patientId); }
    @GetMapping("/patient/{patientId}/past") public List<AppointmentResponse> past(@PathVariable String patientId) { return service.past(patientId); }
    @GetMapping("/doctor/{doctorId}/daily") public List<AppointmentResponse> daily(@PathVariable Long doctorId, @RequestParam LocalDate date) { return service.daily(date, doctorId); }
    @GetMapping("/daily") public List<AppointmentResponse> dailyAll(@RequestParam LocalDate date) { return service.daily(date, null); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public AppointmentResponse create(@Valid @RequestBody CreateAppointmentRequest request) { return service.create(request); }
    @PutMapping("/{id}/reschedule") public AppointmentResponse reschedule(@PathVariable Long id, @Valid @RequestBody RescheduleRequest request) { return service.reschedule(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void cancel(@PathVariable Long id) { service.cancel(id); }
}
