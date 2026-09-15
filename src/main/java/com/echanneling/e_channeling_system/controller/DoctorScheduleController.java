package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.dao.DoctorScheduleRepository;
import com.echanneling.e_channeling_system.model.DoctorSchedule;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class DoctorScheduleController {

    private final DoctorScheduleRepository doctorScheduleRepository;

    public DoctorScheduleController(DoctorScheduleRepository doctorScheduleRepository) {
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    @GetMapping("/doctor-schedule")
    public String showDoctorSchedulePage(Model model) {

        model.addAttribute("doctorSchedule", new DoctorSchedule());

        model.addAttribute(
                "schedules",
                doctorScheduleRepository.findAll()
        );

        return "doctor-schedule";
    }

    @PostMapping("/doctor-schedule/save")
    public String saveDoctorSchedule(
            @Valid @ModelAttribute DoctorSchedule doctorSchedule,
            BindingResult bindingResult,
            Model model) {

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

    @GetMapping("/doctor-schedule/edit/{id}")
    public String editDoctorSchedule(
            @PathVariable int id,
            Model model) {

        DoctorSchedule doctorSchedule =
                doctorScheduleRepository.findById(id).orElse(null);

        model.addAttribute("doctorSchedule", doctorSchedule);

        model.addAttribute(
                "schedules",
                doctorScheduleRepository.findAll()
        );

        return "doctor-schedule";
    }

    @GetMapping("/doctor-schedule/cancel/{id}")
    public String cancelDoctorSchedule(@PathVariable int id) {

        DoctorSchedule doctorSchedule =
                doctorScheduleRepository.findById(id).orElse(null);

        if (doctorSchedule != null) {

            doctorSchedule.setStatus("Cancelled");

            doctorScheduleRepository.save(doctorSchedule);
        }

        return "redirect:/doctor-schedule";
    }

    @GetMapping("/doctor-schedule/delete/{id}")
    public String deleteDoctorSchedule(@PathVariable int id) {

        doctorScheduleRepository.deleteById(id);

        return "redirect:/doctor-schedule";
    }
}