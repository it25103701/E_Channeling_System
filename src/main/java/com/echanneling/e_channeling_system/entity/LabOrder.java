package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Entity representing a Diagnostic Test Requisition and Lab Order (UC-06).
 * Manages clinical orders linked to doctor consultation appointments, tracking
 * test categories, department allocations, urgency priorities, and workflow statuses.
 */
@Entity
@Table(name = "lab_orders")
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "Diagnostic test category must be selected.")
    @Column(name = "test_category", nullable = false)
    private String testCategory;

    @NotBlank(message = "Clinical department is mandatory.")
    @Column(name = "department", nullable = false)
    private String department;

    @NotBlank(message = "Order requisition date is required.")
    @Column(name = "order_date", nullable = false)
    private String orderDate;

    @NotBlank(message = "Expected completion date is required.")
    @Column(name = "expected_date", nullable = false)
    private String expectedDate;

    @NotBlank(message = "Urgency level must be specified.")
    @Pattern(regexp = "^(?i)(ROUTINE|URGENT|STAT)$", message = "Urgency level must be ROUTINE, URGENT, or STAT.")
    @Column(name = "urgency_level", nullable = false, length = 20)
    private String urgencyLevel;

    @NotNull(message = "Linked appointment reference ID is mandatory.")
    @Column(name = "appointment_ref_id", nullable = false)
    private Long appointmentRefId;

    @Column(name = "status", length = 30)
    private String status;

    public LabOrder() {
    }

    public LabOrder(String testCategory,
                    String department,
                    String orderDate,
                    String expectedDate,
                    String urgencyLevel,
                    Long appointmentRefId,
                    String status) {
        this.testCategory = testCategory;
        this.department = department;
        this.orderDate = orderDate;
        this.expectedDate = expectedDate;
        this.urgencyLevel = urgencyLevel;
        this.appointmentRefId = appointmentRefId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestCategory() {
        return testCategory;
    }

    public void setTestCategory(String testCategory) {
        this.testCategory = testCategory;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Long getAppointmentRefId() {
        return appointmentRefId;
    }

    public void setAppointmentRefId(Long appointmentRefId) {
        this.appointmentRefId = appointmentRefId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LabOrder{" +
                "id=" + id +
                ", testCategory='" + testCategory + '\'' +
                ", department='" + department + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", expectedDate='" + expectedDate + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", appointmentRefId=" + appointmentRefId +
                ", status='" + status + '\'' +
                '}';
    }
}