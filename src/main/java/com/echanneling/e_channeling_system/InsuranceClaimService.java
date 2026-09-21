package com.echanneling.e_channeling_system;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InsuranceClaimService {

    private final InsuranceClaimRepository claimRepository;
    private final ClaimAuditLogRepository auditLogRepository;

    public InsuranceClaimService(InsuranceClaimRepository claimRepository,
                                 ClaimAuditLogRepository auditLogRepository) {
        this.claimRepository = claimRepository;
        this.auditLogRepository = auditLogRepository;
    }

    // CREATE: patient submits a pre-authorisation claim
    public InsuranceClaim submitClaim(InsuranceClaim claim) {
        claim.setCreatedAt(LocalDateTime.now());
        claim.setCoveragePercentage(0);
        claim.setNetPayable(claim.getChannelingFee());

        if (claim.getPolicyExpiryDate() == null
                || claim.getPolicyExpiryDate().isBefore(LocalDate.now())) {
            claim.setStatus("REJECTED");
            claim.setRejectionReason("Policy is expired or invalid");
        } else if (claim.getDeductibleBalance() < claim.getChannelingFee()) {
            claim.setStatus("NEEDS_REVIEW");
        } else {
            claim.setStatus("PENDING");
        }

        InsuranceClaim saved = claimRepository.save(claim);
        if ("REJECTED".equals(saved.getStatus())) {
            logAction(saved.getId(), "REJECTED", saved.getRejectionReason(), "System");
        }
        return saved;
    }

    // READ
    public List<InsuranceClaim> getAllClaims() {
        return claimRepository.findAll();
    }

    public List<InsuranceClaim> getClaimsByPatient(String patientName) {
        return claimRepository.findByPatientName(patientName);
    }

    public InsuranceClaim getClaimById(Long id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found: " + id));
    }

    public List<ClaimAuditLog> getAuditLogs() {
        return auditLogRepository.findAllByOrderByActionTimeDesc();
    }

    // UPDATE: Financial Admin approves or adjusts the coverage percentage
    public InsuranceClaim approveClaim(Long id, double coveragePercentage) {
        if (coveragePercentage < 0 || coveragePercentage > 100) {
            throw new IllegalArgumentException("Coverage must be between 0 and 100");
        }
        InsuranceClaim claim = getClaimById(id);

        double covered = claim.getChannelingFee() * coveragePercentage / 100.0;
        if (covered > claim.getDeductibleBalance()) {
            covered = claim.getDeductibleBalance();
        }

        claim.setCoveragePercentage(coveragePercentage);
        claim.setNetPayable(claim.getChannelingFee() - covered);
        claim.setStatus("APPROVED");
        claim.setRejectionReason(null);
        InsuranceClaim saved = claimRepository.save(claim);

        logAction(id, "APPROVED", "Coverage set to " + coveragePercentage + "%", "Financial Admin");
        return saved;
    }

    // DELETE (soft): reject or cancel with a reason
    public InsuranceClaim rejectClaim(Long id, String reason) {
        InsuranceClaim claim = getClaimById(id);
        claim.setStatus("REJECTED");
        claim.setRejectionReason(reason);
        claim.setCoveragePercentage(0);
        claim.setNetPayable(claim.getChannelingFee());
        InsuranceClaim saved = claimRepository.save(claim);

        logAction(id, "REJECTED", reason, "Financial Admin");
        return saved;
    }

    public InsuranceClaim cancelClaim(Long id, String reason) {
        InsuranceClaim claim = getClaimById(id);
        claim.setStatus("CANCELLED");
        claim.setRejectionReason(reason);
        claim.setCoveragePercentage(0);
        claim.setNetPayable(claim.getChannelingFee());
        InsuranceClaim saved = claimRepository.save(claim);

        logAction(id, "CANCELLED", reason, "Financial Admin");
        return saved;
    }

    private void logAction(Long claimId, String action, String reason, String actedBy) {
        auditLogRepository.save(new ClaimAuditLog(claimId, action, reason, actedBy));
    }
}