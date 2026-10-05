package com.echanneling.e_channeling_system.repository;

import com.echanneling.e_channeling_system.InsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {

    List<InsuranceClaim> findByPatientName(String patientName);

    List<InsuranceClaim> findByStatus(String status);
}
