package com.echanneling.e_channeling_system.pattern.strategy;

import java.time.LocalDate;

public class StatUrgentPriorityStrategy implements OrderPriorityStrategy {
    @Override
    public LocalDate computeTargetCompletionDate(LocalDate orderDate, int standardTurnaroundHours) {
        return orderDate != null ? orderDate : LocalDate.now();
    }

    @Override
    public String getPriorityInstruction() {
        return "CRITICAL / STAT: Immediate expedited sample handling and diagnosis.";
    }
}