package com.echanneling.e_channeling_system.service;

import com.echanneling.e_channeling_system.dao.DoctorRepository;
import com.echanneling.e_channeling_system.dao.DoctorScheduleRepository;
import com.echanneling.e_channeling_system.model.Doctor;
import com.echanneling.e_channeling_system.model.DoctorSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorReplacementService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    public DoctorReplacementService(
            DoctorRepository doctorRepository,
            DoctorScheduleRepository doctorScheduleRepository) {

        this.doctorRepository = doctorRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    @Transactional
    public String reassignDoctor(int scheduleId) {

        // Find the existing schedule
        DoctorSchedule schedule =
                doctorScheduleRepository
                        .findById(scheduleId)
                        .orElse(null);

        if (schedule == null) {
            return "Schedule not found.";
        }

        // Find the doctor currently assigned to the schedule
        Doctor originalDoctor =
                doctorRepository
                        .findById(schedule.getDoctorId())
                        .orElse(null);

        if (originalDoctor == null) {
            return "Original doctor not found.";
        }

        // Mark original doctor as unavailable
        originalDoctor.setAvailabilityStatus("Unavailable");
        doctorRepository.save(originalDoctor);

        // Find doctors with the same specialization
        List<Doctor> replacementDoctors =
                doctorRepository
                        .findBySpecializationIgnoreCaseAndAvailabilityStatusIgnoreCaseAndDoctorIdNot(
                                originalDoctor.getSpecialization(),
                                "Available",
                                originalDoctor.getDoctorId()
                        );

        // Check each replacement doctor
        for (Doctor replacementDoctor : replacementDoctors) {

            long overlapCount =
                    doctorScheduleRepository.countOverlappingSchedules(
                            replacementDoctor.getDoctorId(),
                            schedule.getConsultationDate(),
                            schedule.getStartTime(),
                            schedule.getEndTime(),
                            0
                    );

            // Replacement doctor is free at this time
            if (overlapCount == 0) {

                int oldDoctorId = schedule.getDoctorId();

                // Reassign existing schedule
                schedule.setDoctorId(
                        replacementDoctor.getDoctorId()
                );

                doctorScheduleRepository.save(schedule);

                return "Schedule ID " +
                        schedule.getScheduleId() +
                        " reassigned from Doctor ID " +
                        oldDoctorId +
                        " to Doctor ID " +
                        replacementDoctor.getDoctorId() +
                        " (" +
                        replacementDoctor.getSpecialization() +
                        ").";
            }
        }

        return "No available replacement doctor found for specialization: " +
                originalDoctor.getSpecialization();
    }
}