package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "lab_orders")
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Diagnostic test category must be selected.")
    private String testCategory;       // e.g., Hematology (FBC), Biochemistry (Lipid Profile), Radiology (X-Ray)

    @NotBlank(message = "Clinical department is mandatory.")
    private String department;         // e.g., Cardiology, General OPD, Pediatrics

    @NotBlank(message = "Order requisition date is required.")
    private String orderDate;          // e.g., 2026-09-17

    @NotBlank(message = "Expected completion date is required.")
    private String expectedDate;       // e.g., 2026-09-20

    @NotBlank(message = "Urgency level must be specified.")
    @Pattern(regexp = "^(?i)(ROUTINE|URGENT|STAT)$", message = "Urgency level must be ROUTINE, URGENT, or STAT.")
    private String urgencyLevel;       // ROUTINE, URGENT, STAT (Emergency)

    @NotNull(message = "Linked appointment reference ID is mandatory.")
    private Long appointmentRefId;     // Reference link to consultation appointment

    private String status;             // PENDING, SAMPLE_COLLECTED, COMPLETED, CANCELLED

    public LabOrder() {}

    public LabOrder(String testCategory, String department, String orderDate,
                    String expectedDate, String urgencyLevel, Long appointmentRefId, String status) {
        this.testCategory = testCategory;
        this.department = department;
        this.orderDate = orderDate;
        this.expectedDate = expectedDate;
        this.urgencyLevel = urgencyLevel;
        this.appointmentRefId = appointmentRefId;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTestCategory() { return testCategory; }
    public void setTestCategory(String testCategory) { this.testCategory = testCategory; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getExpectedDate() { return expectedDate; }
    public void setExpectedDate(String expectedDate) { this.expectedDate = expectedDate; }

    public String getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public Long getAppointmentRefId() { return appointmentRefId; }
    public void setAppointmentRefId(Long appointmentRefId) { this.appointmentRefId = appointmentRefId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}