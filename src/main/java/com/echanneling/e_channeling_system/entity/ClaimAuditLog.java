package com.echanneling.e_channeling_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "claim_audit_logs")
public class ClaimAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long claimId;
    private String action;
    private String reason;
    private String actedBy;
    private LocalDateTime actionTime;

    protected ClaimAuditLog() {
    }

    public ClaimAuditLog(Long claimId, String action, String reason, String actedBy) {
        this.claimId = claimId;
        this.action = action;
        this.reason = reason;
        this.actedBy = actedBy;
        this.actionTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getClaimId() {
        return claimId;
    }

    public String getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }

    public String getActedBy() {
        return actedBy;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }
}
