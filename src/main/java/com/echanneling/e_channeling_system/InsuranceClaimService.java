package com.echanneling.e_channeling_system;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InsuranceClaimService {

    private final InsuranceClaimRepository claimRepository;

    public InsuranceClaimService(InsuranceClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
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
            claim.setStatus("NEEDS_REVIEW");   // deductible balance is not enough
        } else {
            claim.setStatus("PENDING");
        }
        return claimRepository.save(claim);
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

    // UPDATE: Financial Admin approves or adjusts the coverage percentage
    public InsuranceClaim approveClaim(Long id, double coveragePercentage) {
        if (coveragePercentage < 0 || coveragePercentage > 100) {
            throw new IllegalArgumentException("Coverage must be between 0 and 100");
        }
        InsuranceClaim claim = getClaimById(id);

        double covered = claim.getChannelingFee() * coveragePercentage / 100.0;
        if (covered > claim.getDeductibleBalance()) {
            covered = claim.getDeductibleBalance();   // cannot cover more than the balance
        }

        claim.setCoveragePercentage(coveragePercentage);
        claim.setNetPayable(claim.getChannelingFee() - covered);
        claim.setStatus("APPROVED");
        claim.setRejectionReason(null);
        return claimRepository.save(claim);
    }

    // DELETE (soft): reject or cancel with a reason
    public InsuranceClaim rejectClaim(Long id, String reason) {
        InsuranceClaim claim = getClaimById(id);
        claim.setStatus("REJECTED");
        claim.setRejectionReason(reason);
        claim.setCoveragePercentage(0);
        claim.setNetPayable(claim.getChannelingFee());
        return claimRepository.save(claim);
    }

    public InsuranceClaim cancelClaim(Long id, String reason) {
        InsuranceClaim claim = getClaimById(id);
        claim.setStatus("CANCELLED");
        claim.setRejectionReason(reason);
        claim.setCoveragePercentage(0);
        claim.setNetPayable(claim.getChannelingFee());
        return claimRepository.save(claim);
    }
}
