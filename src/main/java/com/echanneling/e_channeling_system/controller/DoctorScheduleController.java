package com.echanneling.e_channeling_system.controller;

<<<<<<< HEAD
import com.echanneling.e_channeling_system.dao.DoctorRepository;
import com.echanneling.e_channeling_system.dao.DoctorScheduleRepository;
import com.echanneling.e_channeling_system.model.Doctor;
import com.echanneling.e_channeling_system.model.DoctorSchedule;
import com.echanneling.e_channeling_system.observer.DoctorScheduleNotifier;
import com.echanneling.e_channeling_system.observer.PatientNotificationObserver;
import com.echanneling.e_channeling_system.service.DoctorReplacementService;

=======
import com.echanneling.e_channeling_system.repository.DoctorScheduleRepository;
import com.echanneling.e_channeling_system.entity.DoctorSchedule;
>>>>>>> main
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class DoctorScheduleController {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorReplacementService doctorReplacementService;

    // Observer Pattern - Concrete Subject
    private final DoctorScheduleNotifier doctorScheduleNotifier;


    public DoctorScheduleController(
            DoctorScheduleRepository doctorScheduleRepository,
            DoctorRepository doctorRepository,
            DoctorReplacementService doctorReplacementService) {

        this.doctorScheduleRepository = doctorScheduleRepository;
        this.doctorRepository = doctorRepository;
        this.doctorReplacementService = doctorReplacementService;

        // Create Concrete Subject
        this.doctorScheduleNotifier =
                new DoctorScheduleNotifier();

        // Register Concrete Observer
        this.doctorScheduleNotifier.addObserver(
                new PatientNotificationObserver()
        );
    }


    // =========================================
    // COMMON PAGE DATA
    // =========================================

    private void addCommonPageData(Model model) {

        List<Doctor> doctors =
                doctorRepository.findAll();

        model.addAttribute(
                "schedules",
                doctorScheduleRepository.findAll()
        );

        model.addAttribute(
                "doctors",
                doctors
        );

        Map<Integer, Doctor> doctorMap =
                doctors.stream()
                        .collect(
                                Collectors.toMap(
                                        Doctor::getDoctorId,
                                        doctor -> doctor
                                )
                        );

        model.addAttribute(
                "doctorMap",
                doctorMap
        );
    }


    // =========================================
    // READ
    // =========================================

    @GetMapping("/doctor-schedule")
    public String showDoctorSchedulePage(
            Model model) {

        model.addAttribute(
                "doctorSchedule",
                new DoctorSchedule()
        );

        addCommonPageData(model);

        return "doctor-schedule";
    }


    // =========================================
    // CREATE / UPDATE
    // =========================================

    @PostMapping("/doctor-schedule/save")
    public String saveDoctorSchedule(
            @Valid @ModelAttribute DoctorSchedule doctorSchedule,
            BindingResult bindingResult,
            Model model) {


        // Check whether this is CREATE or UPDATE
        boolean isUpdate =
                doctorSchedule.getScheduleId() != 0;


        // Keep old schedule details before updating
        DoctorSchedule existingSchedule = null;

        if (isUpdate) {

            existingSchedule =
                    doctorScheduleRepository
                            .findById(
                                    doctorSchedule.getScheduleId()
                            )
                            .orElse(null);
        }


        // =====================================
        // VALIDATION 1 - DOCTOR EXISTS
        // =====================================

        Doctor selectedDoctor = null;

        if (doctorSchedule.getDoctorId() > 0) {

            selectedDoctor =
                    doctorRepository
                            .findById(
                                    doctorSchedule.getDoctorId()
                            )
                            .orElse(null);

            if (selectedDoctor == null) {

                bindingResult.rejectValue(
                        "doctorId",
                        "doctorNotFound",
                        "Selected doctor does not exist"
                );
            }
        }


        // =====================================
        // VALIDATION 2 - DOCTOR AVAILABLE
        // =====================================

        if (selectedDoctor != null &&
                !"Available".equalsIgnoreCase(
                        selectedDoctor.getAvailabilityStatus())) {

            bindingResult.rejectValue(
                    "doctorId",
                    "doctorUnavailable",
                    "Selected doctor is currently unavailable"
            );
        }


        // =====================================
        // VALIDATION 3 - PAST DATE
        // =====================================

        if (doctorSchedule.getConsultationDate() != null &&
                doctorSchedule.getConsultationDate()
                        .isBefore(LocalDate.now())) {

            bindingResult.rejectValue(
                    "consultationDate",
                    "pastDate",
                    "Consultation date cannot be in the past"
            );
        }


        // =====================================
        // VALIDATION 4 - START / END TIME
        // =====================================

        if (doctorSchedule.getStartTime() != null &&
                doctorSchedule.getEndTime() != null &&
                !doctorSchedule.getStartTime()
                        .isBefore(
                                doctorSchedule.getEndTime()
                        )) {

            bindingResult.rejectValue(
                    "endTime",
                    "invalidTime",
                    "End time must be after start time"
            );
        }


        // =====================================
        // VALIDATION 5 - DOCTOR OVERLAP
        // =====================================

        if (doctorSchedule.getConsultationDate() != null &&
                doctorSchedule.getStartTime() != null &&
                doctorSchedule.getEndTime() != null &&
                doctorSchedule.getStartTime()
                        .isBefore(
                                doctorSchedule.getEndTime()
                        ) &&
                !"Cancelled".equalsIgnoreCase(
                        doctorSchedule.getStatus())) {

            long overlappingSchedules =
                    doctorScheduleRepository
                            .countOverlappingSchedules(
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


        // =====================================
        // VALIDATION 6 - ROOM OVERLAP
        // =====================================

        if (doctorSchedule.getRoomNo() != null &&
                !doctorSchedule.getRoomNo().isBlank() &&
                doctorSchedule.getConsultationDate() != null &&
                doctorSchedule.getStartTime() != null &&
                doctorSchedule.getEndTime() != null &&
                doctorSchedule.getStartTime()
                        .isBefore(
                                doctorSchedule.getEndTime()
                        ) &&
                !"Cancelled".equalsIgnoreCase(
                        doctorSchedule.getStatus())) {

            long roomOverlap =
                    doctorScheduleRepository
                            .countOverlappingRoomSchedules(
                                    doctorSchedule.getRoomNo(),
                                    doctorSchedule.getConsultationDate(),
                                    doctorSchedule.getStartTime(),
                                    doctorSchedule.getEndTime(),
                                    doctorSchedule.getScheduleId()
                            );

            if (roomOverlap > 0) {

                bindingResult.rejectValue(
                        "roomNo",
                        "roomConflict",
                        "Room is already assigned during this time"
                );
            }
        }


        // =====================================
        // CHECK VALIDATION ERRORS
        // =====================================

        if (bindingResult.hasErrors()) {

            addCommonPageData(model);

            return "doctor-schedule";
        }


        // =====================================
        // CHECK WHETHER UPDATE CHANGED DETAILS
        // =====================================

        boolean scheduleChanged = false;

        if (isUpdate && existingSchedule != null) {

            scheduleChanged =

                    existingSchedule.getDoctorId()
                            != doctorSchedule.getDoctorId()

                            ||

                            !Objects.equals(
                                    existingSchedule.getConsultationDate(),
                                    doctorSchedule.getConsultationDate()
                            )

                            ||

                            !Objects.equals(
                                    existingSchedule.getStartTime(),
                                    doctorSchedule.getStartTime()
                            )

                            ||

                            !Objects.equals(
                                    existingSchedule.getEndTime(),
                                    doctorSchedule.getEndTime()
                            )

                            ||

                            existingSchedule.getSlotCapacity()
                                    != doctorSchedule.getSlotCapacity()

                            ||

                            !Objects.equals(
                                    existingSchedule.getRoomNo(),
                                    doctorSchedule.getRoomNo()
                            )

                            ||

                            !Objects.equals(
                                    existingSchedule.getStatus(),
                                    doctorSchedule.getStatus()
                            );
        }


        // =====================================
        // SAVE
        // =====================================

        doctorScheduleRepository.save(
                doctorSchedule
        );


        // =====================================
        // OBSERVER - UPDATE NOTIFICATION
        // =====================================

        if (isUpdate && scheduleChanged) {

            String message =
                    "Schedule ID " +
                            doctorSchedule.getScheduleId() +
                            " has been updated. " +
                            "Doctor ID: " +
                            doctorSchedule.getDoctorId() +
                            ", Date: " +
                            doctorSchedule.getConsultationDate() +
                            ", Time: " +
                            doctorSchedule.getStartTime() +
                            " - " +
                            doctorSchedule.getEndTime() +
                            ", Room: " +
                            doctorSchedule.getRoomNo() +
                            ".";

            doctorScheduleNotifier
                    .notifyObservers(message);
        }


        return "redirect:/doctor-schedule";
    }


    // =========================================
    // EDIT
    // =========================================

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

        addCommonPageData(model);

        return "doctor-schedule";
    }


    // =========================================
    // CANCEL + OBSERVER PATTERN
    // =========================================

    @GetMapping("/doctor-schedule/cancel/{id}")
    public String cancelDoctorSchedule(
            @PathVariable int id) {

        DoctorSchedule doctorSchedule =
                doctorScheduleRepository
                        .findById(id)
                        .orElse(null);

        if (doctorSchedule != null) {

            doctorSchedule.setStatus(
                    "Cancelled"
            );

            doctorScheduleRepository.save(
                    doctorSchedule
            );

            String message =
                    "Schedule ID " +
                            doctorSchedule.getScheduleId() +
                            " for Doctor ID " +
                            doctorSchedule.getDoctorId() +
                            " has been cancelled.";

            doctorScheduleNotifier
                    .notifyObservers(message);
        }

        return "redirect:/doctor-schedule";
    }


    // =========================================
    // EMERGENCY DOCTOR REASSIGNMENT
    // =========================================

    @GetMapping("/doctor-schedule/reassign/{id}")
    public String reassignDoctor(
            @PathVariable int id,
            RedirectAttributes redirectAttributes) {

        String result =
                doctorReplacementService
                        .reassignDoctor(id);

        redirectAttributes.addFlashAttribute(
                "reassignmentMessage",
                result
        );

        if (result.contains(
                "reassigned from Doctor ID")) {

            doctorScheduleNotifier
                    .notifyObservers(
                            "Doctor reassignment: " + result
                    );
        }

        return "redirect:/doctor-schedule";
    }


    // =========================================
    // DELETE
    // =========================================

    @GetMapping("/doctor-schedule/delete/{id}")
    public String deleteDoctorSchedule(
            @PathVariable int id) {

        doctorScheduleRepository
                .deleteById(id);

        return "redirect:/doctor-schedule";
    }
}