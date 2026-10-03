package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;
    private String type;            // e.g., "SYSTEM", "CRITICAL", "GENERAL"
    private String preferenceRules; // e.g., "All Doctors / Patients"

    // Fields for Direct vs Broadcast targeting
    private String recipientType;   // "BROADCAST" or "DIRECT"
    private String patientId;       // Single ID or comma-separated IDs (e.g., "P-101, P-102")

    public Notification() {}

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

    public String getPreferenceRules() {
        return preferenceRules;
    }

    public void setPreferenceRules(String preferenceRules) {
        this.preferenceRules = preferenceRules;
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
}