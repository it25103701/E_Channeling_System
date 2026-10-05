package com.medibook.appointment.dto;

import com.medibook.appointment.entity.Doctor;

public record DoctorResponse(Long id, String name, String specialisation, String qualification, String clinic) {
    public static DoctorResponse from(Doctor doctor) {
        return new DoctorResponse(doctor.getId(), doctor.getName(), doctor.getSpecialisation(),
                doctor.getQualification(), doctor.getClinic());
    }
}
