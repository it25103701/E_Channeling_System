package com.echanneling.e_channeling_system.pattern.strategy;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Context class for the Strategy Pattern implementation in Lab Orders (UC-06).
 * Maintains a reference to an {@link OrderPriorityStrategy} and coordinates
 * target completion date computation across various clinical urgency levels.
 */
@Component
public class OrderProcessingContext {

    private OrderPriorityStrategy strategy;

    /**
     * Default constructor initializing with a standard routine strategy.
     */
    public OrderProcessingContext() {
        this.strategy = new RoutinePriorityStrategy();
    }

    /**
     * Parameterized constructor allowing injection of a specific strategy.
     *
     * @param strategy initial order priority strategy
     */
    public OrderProcessingContext(OrderPriorityStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Configures or swaps the active order priority strategy dynamically.
     *
     * @param strategy the priority resolution strategy to apply
     */
    public void setStrategy(OrderPriorityStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Retrieves the currently active priority strategy.
     *
     * @return current {@link OrderPriorityStrategy}
     */
    public OrderPriorityStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * Determines the final completion deadline by executing the active strategy.
     * Falls back to {@link RoutinePriorityStrategy} if no strategy is explicitly configured.
     *
     * @param orderDate requisition placement date
     * @param baseHours standard turnaround hours for the diagnostic category
     * @return computed deadline date
     */
    public LocalDate determineTargetDate(LocalDate orderDate, int baseHours) {
        if (this.strategy == null) {
            this.strategy = new RoutinePriorityStrategy();
        }
        return this.strategy.computeTargetCompletionDate(orderDate, baseHours);
    }
}