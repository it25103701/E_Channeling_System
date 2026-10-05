package com.echanneling.e_channeling_system.pattern.strategy;

import java.time.LocalDate;

/**
 * Strategy interface defining priority handling and turnaround computation for Lab Orders (UC-06).
 * Encapsulates the algorithmic variations for computing diagnostic deadlines and clinical instructions
 * across different urgency tiers (e.g., ROUTINE, URGENT, STAT).
 */
public interface OrderPriorityStrategy {

    /**
     * Calculates the estimated target completion date based on priority tier specifications.
     *
     * @param orderDate               the date the diagnostic requisition was placed
     * @param standardTurnaroundHours baseline processing turnaround hours required for the test
     * @return the computed completion deadline date
     */
    LocalDate computeTargetCompletionDate(LocalDate orderDate, int standardTurnaroundHours);

    /**
     * Retrieves the clinical dispatch directive and workflow handling instructions for this priority level.
     *
     * @return priority-specific handling instruction
     */
    String getPriorityInstruction();
}