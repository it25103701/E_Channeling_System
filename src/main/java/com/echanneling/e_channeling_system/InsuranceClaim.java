package com.echanneling.e_channeling_system;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who and which appointment (plain fields for now, teammates' tables will be linked later)
    private String patientName;
    private Long appointmentId;

    // Policy details submitted by the patient
    private String policyNumber;
    private String insurerName;
    private LocalDate policyExpiryDate;
    private double deductibleBalance;

    // Money
    private double channelingFee;
    private double coveragePercentage;
    private double netPayable;

    // PENDING, APPROVED, REJECTED, CANCELLED
    private String status;
    private String rejectionReason;
    private LocalDateTime createdAt;

    public InsuranceClaim() {
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(double netPayable) {
        this.netPayable = netPayable;
    }

    public double getCoveragePercentage() {
        return coveragePercentage;
    }

    public void setCoveragePercentage(double coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    public double getChannelingFee() {
        return channelingFee;
    }

    public void setChannelingFee(double channelingFee) {
        this.channelingFee = channelingFee;
    }

    public double getDeductibleBalance() {
        return deductibleBalance;
    }

    public void setDeductibleBalance(double deductibleBalance) {
        this.deductibleBalance = deductibleBalance;
    }

    public LocalDate getPolicyExpiryDate() {
        return policyExpiryDate;
    }

    public void setPolicyExpiryDate(LocalDate policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
