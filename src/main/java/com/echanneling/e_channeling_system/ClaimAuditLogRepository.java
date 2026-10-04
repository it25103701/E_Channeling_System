package com.echanneling.e_channeling_system;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimAuditLogRepository extends JpaRepository<ClaimAuditLog, Long> {

    List<ClaimAuditLog> findAllByOrderByActionTimeDesc();
}