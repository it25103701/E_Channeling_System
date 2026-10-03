package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String name;
    @NotBlank private String specialisation;
    private String qualification;
    private String clinic;
    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Slot> slots = new ArrayList<>();

    public Doctor() { }
    public Doctor(String name, String specialisation, String qualification, String clinic) {
        this.name = name; this.specialisation = specialisation; this.qualification = qualification; this.clinic = clinic;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialisation() { return specialisation; }
    public void setSpecialisation(String specialisation) { this.specialisation = specialisation; }
    public String getQualification() { return qualification; }
    public String getClinic() { return clinic; }
}