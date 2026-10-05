package com.echanneling.e_channeling_system.pattern.strategy;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Priority resolution strategy for standard routine diagnostic orders (UC-06).
 * Schedules completions according to default turnaround cycles.
 */
@Component
public class RoutinePriorityStrategy implements OrderPriorityStrategy {

    /**
     * Calculates the estimated target completion date for standard routine orders.
     * Applies a 48-hour (2-day) standard diagnostic processing window.
     *
     * @param orderDate               the date the requisition was submitted
     * @param standardTurnaroundHours baseline processing turnaround hours
     * @return the computed completion target date
     */
    @Override
    public LocalDate computeTargetCompletionDate(LocalDate orderDate, int standardTurnaroundHours) {
        LocalDate baseDate = (orderDate != null) ? orderDate : LocalDate.now();
        return baseDate.plusDays(2);
    }

    /**
     * Supplies standard handling instructions for routine laboratory queue processing.
     *
     * @return routine queuing directive
     */
    @Override
    public String getPriorityInstruction() {
        return "Standard laboratory routine processing queue.";
    }
}