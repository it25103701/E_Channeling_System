package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;
    private String type;
    private String recipientType;
    private String patientId;
    private String preferenceRules;

    public Notification() {
    }

    public Notification(String title, String message, String type, String recipientType, String patientId, String preferenceRules) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.recipientType = recipientType;
        this.patientId = patientId;
        this.preferenceRules = preferenceRules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPreferenceRules() {
        return preferenceRules;
    }

    public void setPreferenceRules(String preferenceRules) {
        this.preferenceRules = preferenceRules;
    }
}