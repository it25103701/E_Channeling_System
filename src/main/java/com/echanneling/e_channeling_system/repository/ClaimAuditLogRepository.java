package com.echanneling.e_channeling_system.repository;

import com.echanneling.e_channeling_system.entity.ClaimAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimAuditLogRepository extends JpaRepository<ClaimAuditLog, Long> {
    List<ClaimAuditLog> findByClaimId(Long claimId);
}