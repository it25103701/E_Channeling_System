package com.echanneling.e_channeling_system;
import com.echanneling.e_channeling_system.pattern.factory.DiagnosticTest;
import com.echanneling.e_channeling_system.pattern.factory.DiagnosticTestFactory;
import com.echanneling.e_channeling_system.pattern.strategy.OrderProcessingContext;
import com.echanneling.e_channeling_system.pattern.strategy.RoutinePriorityStrategy;
import com.echanneling.e_channeling_system.pattern.strategy.StatUrgentPriorityStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LabDiagnosticPatternTests {

    @Test
    @DisplayName("Factory Pattern: Resolves correct clinical departments and turnaround baselines")
    void testFactoryPatternCreation() {
        DiagnosticTest bloodTest = DiagnosticTestFactory.createTest("Full Blood Count (FBC)");
        assertNotNull(bloodTest, "Factory must return a non-null DiagnosticTest instance");
        assertTrue(bloodTest.getDefaultDepartment().contains("Pathology"),
                "Expected department to contain 'Pathology', but got: " + bloodTest.getDefaultDepartment());

        DiagnosticTest ecgTest = DiagnosticTestFactory.createTest("12-Lead ECG");
        assertNotNull(ecgTest, "Factory must return a non-null DiagnosticTest instance");
        assertTrue(ecgTest.getDefaultDepartment().contains("Cardiology"),
                "Expected department to contain 'Cardiology', but got: " + ecgTest.getDefaultDepartment());
    }

    @Test
    @DisplayName("Strategy Pattern: STAT priority restricts target turnaround within 24 hours")
    void testStatStrategyTurnaround() {
        OrderProcessingContext context = new OrderProcessingContext();
        context.setStrategy(new StatUrgentPriorityStrategy());

        LocalDate orderDate = LocalDate.now();
        LocalDate targetDate = context.determineTargetDate(orderDate, 4);

        assertNotNull(targetDate, "Target turnaround date must not be null");
        assertFalse(targetDate.isAfter(orderDate.plusDays(1)),
                "STAT emergency strategy must enforce delivery within 24 hours");
    }

    @Test
    @DisplayName("Strategy Pattern: Routine priority schedules standard multiday turnaround")
    void testRoutineStrategyTurnaround() {
        OrderProcessingContext context = new OrderProcessingContext();
        context.setStrategy(new RoutinePriorityStrategy());

        LocalDate orderDate = LocalDate.now();
        LocalDate targetDate = context.determineTargetDate(orderDate, 48);

        assertNotNull(targetDate, "Target turnaround date must not be null");
        assertTrue(targetDate.isAfter(orderDate) || targetDate.isEqual(orderDate.plusDays(2)),
                "Routine strategy must calculate multi-day diagnostic queues");
    }
}