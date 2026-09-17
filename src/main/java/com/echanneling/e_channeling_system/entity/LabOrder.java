package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lab_orders")
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testCategory;       // e.g., Hematology (FBC), Biochemistry (Lipid Profile), Radiology (X-Ray)
    private String department;         // e.g., Cardiology, General OPD, Pediatrics
    private String orderDate;          // e.g., 2026-09-17
    private String expectedDate;       // e.g., 2026-09-20
    private String urgencyLevel;       // ROUTINE, URGENT, STAT (Emergency)
    private Long appointmentRefId;     // Reference link to Damsana's Appointment
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