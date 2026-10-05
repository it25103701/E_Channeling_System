package com.medibook.appointment.controller;
import com.medibook.appointment.dto.DoctorRequest;
import com.medibook.appointment.dto.DoctorResponse;
import com.medibook.appointment.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService service;
    public DoctorController(DoctorService service) { this.service = service; }
    @GetMapping public List<DoctorResponse> all(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String specialisation) { return service.search(name, specialisation); }
    @GetMapping("/search") public List<DoctorResponse> search(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String specialisation) { return service.search(name, specialisation); }
    @GetMapping("/{id}") public DoctorResponse get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public DoctorResponse create(@Valid @RequestBody DoctorRequest request) { return service.create(request); }
    @PutMapping("/{id}") public DoctorResponse update(@PathVariable Long id, @Valid @RequestBody DoctorRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
