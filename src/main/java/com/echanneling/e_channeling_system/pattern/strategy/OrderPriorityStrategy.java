package com.echanneling.e_channeling_system.pattern.strategy;

import java.time.LocalDate;

public interface OrderPriorityStrategy {
    LocalDate computeTargetCompletionDate(LocalDate orderDate, int standardTurnaroundHours);
    String getPriorityInstruction();
}