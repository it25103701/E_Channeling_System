package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.*;
import com.echanneling.e_channeling_system.repository.SlotRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {
    private final SlotRepository repository;

    public SlotController(SlotRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Slot> available(@RequestParam(required = false) Long doctorId,
                                @RequestParam(required = false) LocalDate from,
                                @RequestParam(required = false) LocalDate to,
                                @RequestParam(required = false) LocalDate date) {
        if (date != null)
            return repository.findByDateAndStatusOrderByTimeAsc(date, SlotStatus.AVAILABLE);
        if (doctorId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "doctorId or date is required");
        return repository.findByDoctorIdAndDateBetweenAndStatusOrderByDateAscTimeAsc(
                doctorId,
                from == null ? LocalDate.now() : from,
                to == null ? LocalDate.now().plusMonths(1) : to,
                SlotStatus.AVAILABLE);
    }
}