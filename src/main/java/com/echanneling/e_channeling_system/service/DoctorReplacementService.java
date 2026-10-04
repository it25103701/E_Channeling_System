package com.echanneling.e_channeling_system.service;

import com.echanneling.e_channeling_system.entity.Doctor;
import com.echanneling.e_channeling_system.entity.DoctorSchedule;
import com.echanneling.e_channeling_system.repository.DoctorRepository;
import com.echanneling.e_channeling_system.repository.DoctorScheduleRepository;
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

        // 1. Find the existing schedule
        DoctorSchedule schedule = doctorScheduleRepository
                .findById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return "Schedule not found.";
        }

        // 2. Find the doctor currently assigned to the schedule
        Doctor originalDoctor = doctorRepository
                .findById((long) schedule.getDoctorId())
                .orElse(null);

        if (originalDoctor == null) {
            return "Original doctor not found.";
        }

        // 3. Find replacement doctors with the exact same specialization (excluding current doctor)
        List<Doctor> replacementDoctors = doctorRepository
                .findBySpecialisationIgnoreCaseAndIdNot(
                        originalDoctor.getSpecialisation(),
                        originalDoctor.getId()
                );

        // 4. Find the first replacement doctor who doesn't have an overlapping schedule
        for (Doctor replacementDoctor : replacementDoctors) {

            long overlapCount = doctorScheduleRepository.countOverlappingSchedules(
                    replacementDoctor.getId().intValue(),
                    schedule.getConsultationDate(),
                    schedule.getStartTime(),
                    schedule.getEndTime(),
                    0
            );

            // Reassign if doctor is free
            if (overlapCount == 0) {
                int oldDoctorId = schedule.getDoctorId();
                schedule.setDoctorId(replacementDoctor.getId().intValue());
                doctorScheduleRepository.save(schedule);

                return "Schedule ID " + schedule.getScheduleId() +
                        " reassigned from Doctor ID " + oldDoctorId +
                        " to Doctor ID " + replacementDoctor.getId() +
                        " (" + replacementDoctor.getSpecialisation() + ").";
            }
        }

        return "No available replacement doctor found for specialization: " +
                originalDoctor.getSpecialisation();
    }
}