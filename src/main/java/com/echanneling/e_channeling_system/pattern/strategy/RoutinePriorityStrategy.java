package com.echanneling.e_channeling_system.pattern.strategy;

import java.time.LocalDate;

public class RoutinePriorityStrategy implements OrderPriorityStrategy {
    @Override
    public LocalDate computeTargetCompletionDate(LocalDate orderDate, int standardTurnaroundHours) {
        return (orderDate != null ? orderDate : LocalDate.now()).plusDays(2);
    }

    @Override
    public String getPriorityInstruction() {
        return "Standard laboratory routine processing queue.";
    }
}