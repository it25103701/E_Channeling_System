package com.echanneling.e_channeling_system.pattern.strategy;

import java.time.LocalDate;

public class OrderProcessingContext {
    private OrderPriorityStrategy strategy;

    public void setStrategy(OrderPriorityStrategy strategy) {
        this.strategy = strategy;
    }

    public LocalDate determineTargetDate(LocalDate orderDate, int baseHours) {
        if (this.strategy == null) {
            this.strategy = new RoutinePriorityStrategy();
        }
        return this.strategy.computeTargetCompletionDate(orderDate, baseHours);
    }
}