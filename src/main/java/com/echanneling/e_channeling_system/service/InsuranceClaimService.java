package com.echanneling.e_channeling_system.service;

import com.echanneling.e_channeling_system.entity.ClaimAuditLog;
import com.echanneling.e_channeling_system.entity.InsuranceClaim;
import com.echanneling.e_channeling_system.repository.ClaimAuditLogRepository;
import com.echanneling.e_channeling_system.repository.InsuranceClaimRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceClaimService {

    private final InsuranceClaimRepository claimRepository;
    private final ClaimAuditLogRepository auditLogRepository;

    public InsuranceClaimService(InsuranceClaimRepository claimRepository, ClaimAuditLogRepository auditLogRepository) {
        this.claimRepository = claimRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public InsuranceClaim submitClaim(InsuranceClaim claim) {
        claim.setStatus("PENDING");
        InsuranceClaim saved = claimRepository.save(claim);
        auditLogRepository.save(new ClaimAuditLog(saved.getId(), "SUBMITTED", "Claim submitted for amount: " + claim.getClaimAmount()));
        return saved;
    }

    public InsuranceClaim getClaimById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }

    public List<InsuranceClaim> getAllClaims() {
        return claimRepository.findAll();
    }

    public void approveClaim(Long id, double coveragePercentage) {
        InsuranceClaim claim = claimRepository.findById(id).orElse(null);
        if (claim != null) {
            claim.setCoveragePercentage(coveragePercentage);
            claim.setApprovedAmount(claim.getClaimAmount() * (coveragePercentage / 100.0));
            claim.setStatus("APPROVED");
            claimRepository.save(claim);
            auditLogRepository.save(new ClaimAuditLog(id, "APPROVED", "Approved with " + coveragePercentage + "% coverage"));
        }
    }

    public void rejectClaim(Long id, String reason) {
        InsuranceClaim claim = claimRepository.findById(id).orElse(null);
        if (claim != null) {
            claim.setStatus("REJECTED");
            claim.setRejectionReason(reason);
            claimRepository.save(claim);
            auditLogRepository.save(new ClaimAuditLog(id, "REJECTED", "Reason: " + reason));
        }
    }

    public void cancelClaim(Long id, String reason) {
        InsuranceClaim claim = claimRepository.findById(id).orElse(null);
        if (claim != null) {
            claim.setStatus("CANCELLED");
            claim.setRejectionReason(reason);
            claimRepository.save(claim);
            auditLogRepository.save(new ClaimAuditLog(id, "CANCELLED", "Reason: " + reason));
        }
    }

    public List<ClaimAuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }

    public void deleteClaim(Long id) {
        claimRepository.deleteById(id);
        auditLogRepository.save(new ClaimAuditLog(id, "DELETED", "Claim deleted"));
    }
}