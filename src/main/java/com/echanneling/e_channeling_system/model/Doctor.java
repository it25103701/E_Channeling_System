package com.echanneling.e_channeling_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    private int doctorId;

    private String doctorName;

    private String specialization;

    private String availabilityStatus;

    // Required by JPA
    public Doctor() {
    }

    public Doctor(int doctorId,
                  String doctorName,
                  String specialization,
                  String availabilityStatus) {

        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.availabilityStatus = availabilityStatus;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}