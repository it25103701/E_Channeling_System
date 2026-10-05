package com.echanneling.e_channeling_system.repository;

import com.echanneling.e_channeling_system.entity.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link LabOrder} entities (UC-06).
 * Handles persistence, retrieval, and status lookups for diagnostic requisitions.
 */
@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {

    /**
     * Finds diagnostic lab orders associated with a specific consultation appointment.
     *
     * @param appointmentRefId consultation appointment reference identifier
     * @return list of matching lab orders
     */
    List<LabOrder> findByAppointmentRefId(Long appointmentRefId);

    /**
     * Finds diagnostic lab orders filtered by execution status.
     *
     * @param status operational status (e.g., PENDING, SAMPLE_COLLECTED, COMPLETED)
     * @return list of orders with the specified status
     */
    List<LabOrder> findByStatus(String status);
}