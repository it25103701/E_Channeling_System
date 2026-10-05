package com.medibook.appointment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {
    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
                "application", "MediBook Appointment Booking API",
                "status", "running",
                "frontend", "http://localhost:5500/"
        );
    }
}