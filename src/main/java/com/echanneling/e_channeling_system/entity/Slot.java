package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "slots", uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "date", "time"}))
public class Slot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @Column(name = "date", nullable = false) private LocalDate date;
    @Column(name = "time", nullable = false) private LocalTime time;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SlotStatus status = SlotStatus.AVAILABLE;

    public Slot() { }

    public Slot(Doctor doctor, LocalDate date, LocalTime time) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    public Long getId() { return id; }
    public Doctor getDoctor() { return doctor; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }
}