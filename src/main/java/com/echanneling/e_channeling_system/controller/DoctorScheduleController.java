package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.dao.DoctorScheduleRepository;
import com.echanneling.e_channeling_system.model.DoctorSchedule;
import com.echanneling.e_channeling_system.observer.DoctorScheduleNotifier;
import com.echanneling.e_channeling_system.observer.PatientNotificationObserver;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class DoctorScheduleController {

    private final DoctorScheduleRepository doctorScheduleRepository;

    // Observer Pattern - Concrete Subject
    private final DoctorScheduleNotifier doctorScheduleNotifier;

    public DoctorScheduleController(
            DoctorScheduleRepository doctorScheduleRepository) {

        this.doctorScheduleRepository = doctorScheduleRepository;

        // Create Concrete Subject
        this.doctorScheduleNotifier = new DoctorScheduleNotifier();

        // Register Concrete Observer
        this.doctorScheduleNotifier.addObserver(
                new PatientNotificationObserver()
        );
    }

    // =========================
    // READ
    // =========================

    @GetMapping("/doctor-schedule")
    public String showDoctorSchedulePage(Model model) {

        model.addAttribute(
                "doctorSchedule",
                new DoctorSchedule()
        );

        model.addAttribute(
                "schedules",
                doctorScheduleRepository.findAll()
        );

        return "doctor-schedule";
    }

    // =========================
    // CREATE / UPDATE
    // =========================

    @PostMapping("/doctor-schedule/save")
    public String saveDoctorSchedule(
            @Valid @ModelAttribute DoctorSchedule doctorSchedule,
            BindingResult bindingResult,
            Model model) {

        // =========================
        // VALIDATION 1 - PAST DATE
        // =========================

        if (doctorSchedule.getConsultationDate() != null &&
                doctorSchedule.getConsultationDate()
                        .isBefore(LocalDate.now())) {

            bindingResult.rejectValue(
                    "consultationDate",
                    "pastDate",
                    "Consultation date cannot be in the past"
            );
        }

        // =========================
        // VALIDATION 2 - TIME
        // =========================

        if (doctorSchedule.getStartTime() != null &&
                doctorSchedule.getEndTime() != null &&
                !doctorSchedule.getStartTime()
                        .isBefore(doctorSchedule.getEndTime())) {

            bindingResult.rejectValue(
                    "endTime",
                    "invalidTime",
                    "End time must be after start time"
            );
        }

        // =========================
        // VALIDATION 3 - OVERLAP
        // =========================

        if (doctorSchedule.getConsultationDate() != null &&
                doctorSchedule.getStartTime() != null &&
                doctorSchedule.getEndTime() != null &&
                doctorSchedule.getStartTime()
                        .isBefore(doctorSchedule.getEndTime()) &&
                !"Cancelled".equalsIgnoreCase(doctorSchedule.getStatus())) {

            long overlappingSchedules =
                    doctorScheduleRepository.countOverlappingSchedules(
                            doctorSchedule.getDoctorId(),
                            doctorSchedule.getConsultationDate(),
                            doctorSchedule.getStartTime(),
                            doctorSchedule.getEndTime(),
                            doctorSchedule.getScheduleId()
                    );

            if (overlappingSchedules > 0) {

                bindingResult.rejectValue(
                        "startTime",
                        "overlap",
                        "Doctor already has an overlapping schedule"
                );
            }
        }

        // =========================
        // CHECK VALIDATION ERRORS
        // =========================

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "schedules",
                    doctorScheduleRepository.findAll()
            );

            return "doctor-schedule";
        }

        doctorScheduleRepository.save(doctorSchedule);

        return "redirect:/doctor-schedule";
    }

    // =========================
    // EDIT
    // =========================

    @GetMapping("/doctor-schedule/edit/{id}")
    public String editDoctorSchedule(
            @PathVariable int id,
            Model model) {

        DoctorSchedule doctorSchedule =
                doctorScheduleRepository
                        .findById(id)
                        .orElse(null);

        model.addAttribute(
                "doctorSchedule",
                doctorSchedule
        );

        model.addAttribute(
                "schedules",
                doctorScheduleRepository.findAll()
        );

        return "doctor-schedule";
    }

    // =========================
    // CANCEL + OBSERVER PATTERN
    // =========================

    @GetMapping("/doctor-schedule/cancel/{id}")
    public String cancelDoctorSchedule(
            @PathVariable int id) {

        DoctorSchedule doctorSchedule =
                doctorScheduleRepository
                        .findById(id)
                        .orElse(null);

        if (doctorSchedule != null) {

            doctorSchedule.setStatus("Cancelled");

            doctorScheduleRepository.save(doctorSchedule);

            String message =
                    "Schedule ID " +
                            doctorSchedule.getScheduleId() +
                            " for Doctor ID " +
                            doctorSchedule.getDoctorId() +
                            " has been cancelled.";

            doctorScheduleNotifier.notifyObservers(message);
        }

        return "redirect:/doctor-schedule";
    }

    // =========================
    // DELETE
    // =========================

    @GetMapping("/doctor-schedule/delete/{id}")
    public String deleteDoctorSchedule(
            @PathVariable int id) {

        doctorScheduleRepository.deleteById(id);

        return "redirect:/doctor-schedule";
    }
}