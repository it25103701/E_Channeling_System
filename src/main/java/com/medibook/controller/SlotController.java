package com.medibook.appointment.controller;
import com.medibook.appointment.dto.SlotRequest;
import com.medibook.appointment.dto.SlotResponse;
import com.medibook.appointment.service.SlotService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.util.List;
@RestController @RequestMapping("/api/slots")
public class SlotController {
    private final SlotService service;
    public SlotController(SlotService service) { this.service = service; }
    @GetMapping public List<SlotResponse> available(@RequestParam(required = false) Long doctorId, @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to, @RequestParam(required = false) LocalDate date) { return service.available(doctorId, from, to, date); }
    @GetMapping("/{id}") public SlotResponse get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public SlotResponse create(@Valid @RequestBody SlotRequest request) { return service.create(request); }
    @PutMapping("/{id}") public SlotResponse update(@PathVariable Long id, @Valid @RequestBody SlotRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
