package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.Doctor;
import com.echanneling.e_channeling_system.repository.DoctorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository repository;

    public DoctorController(DoctorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Doctor> all(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String specialisation) {

        return repository
                .findByNameContainingIgnoreCaseAndSpecialisationContainingIgnoreCase(
                        name,
                        specialisation
                );
    }

    @GetMapping("/search")
    public List<Doctor> search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String specialisation) {

        return all(name, specialisation);
    }
}