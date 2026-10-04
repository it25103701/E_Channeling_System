package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_schedule")
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;

    @Positive(message = "Doctor ID must be greater than 0")
    private int doctorId;

    @NotNull(message = "Consultation date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate consultationDate;

    @NotNull(message = "Start time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Min(value = 1, message = "Slot capacity must be at least 1")
    private int slotCapacity;

    @NotBlank(message = "Room number is required")
    private String roomNo;

    private String status;


    // Required by JPA
    public DoctorSchedule() {
    }


    public DoctorSchedule(
            int doctorId,
            LocalDate consultationDate,
            LocalTime startTime,
            LocalTime endTime,
            int slotCapacity,
            String roomNo,
            String status) {

        this.doctorId = doctorId;
        this.consultationDate = consultationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotCapacity = slotCapacity;
        this.roomNo = roomNo;
        this.status = status;
    }


    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }


    public LocalDate getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDate consultationDate) {
        this.consultationDate = consultationDate;
    }


    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }


    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    public int getSlotCapacity() {
        return slotCapacity;
    }

    public void setSlotCapacity(int slotCapacity) {
        this.slotCapacity = slotCapacity;
    }


    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}