package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.LabOrder;
import com.echanneling.e_channeling_system.repository.AppointmentRepository;
import com.echanneling.e_channeling_system.repository.LabOrderRepository;
import com.echanneling.e_channeling_system.pattern.factory.DiagnosticTest;
import com.echanneling.e_channeling_system.pattern.factory.DiagnosticTestFactory;
import com.echanneling.e_channeling_system.pattern.strategy.OrderProcessingContext;
import com.echanneling.e_channeling_system.pattern.strategy.RoutinePriorityStrategy;
import com.echanneling.e_channeling_system.pattern.strategy.StatUrgentPriorityStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/lab-orders")
public class LabOrderController {

    private final LabOrderRepository labOrderRepository;
    private final AppointmentRepository appointmentRepository;

    public LabOrderController(LabOrderRepository labOrderRepository,
                              AppointmentRepository appointmentRepository) {
        this.labOrderRepository = labOrderRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // READ: View all active diagnostic orders
    @GetMapping
    public String getAllLabOrders(Model model) {
        model.addAttribute("labOrders", labOrderRepository.findAll());
        model.addAttribute("appointmentCount", appointmentRepository.count());
        return "lab-orders";
    }

    // CREATE: Place a new lab diagnostic order using Factory & Strategy patterns
    @PostMapping("/create")
    public String createLabOrder(@RequestParam String testCategory,
                                 @RequestParam(required = false) String department,
                                 @RequestParam(required = false) String orderDate,
                                 @RequestParam(required = false) String expectedDate,
                                 @RequestParam String urgencyLevel) {

        // 1. Factory Pattern: Instantiate test profile details (department & turnaround)
        DiagnosticTest diagnosticTest = DiagnosticTestFactory.createTest(testCategory);
        String finalDepartment = (department != null && !department.isBlank())
                ? department
                : diagnosticTest.getDefaultDepartment();

        // Parse order date (fallback to today if missing)
        LocalDate parsedOrderDate = (orderDate != null && !orderDate.isBlank())
                ? LocalDate.parse(orderDate)
                : LocalDate.now();

        // 2. Strategy Pattern: Select turnaround algorithm based on clinical urgency
        OrderProcessingContext context = new OrderProcessingContext();
        if ("STAT".equalsIgnoreCase(urgencyLevel) || "URGENT".equalsIgnoreCase(urgencyLevel)) {
            context.setStrategy(new StatUrgentPriorityStrategy());
        } else {
            context.setStrategy(new RoutinePriorityStrategy());
        }

        // Auto-calculate expected completion date if not provided
        String finalExpectedDate = (expectedDate != null && !expectedDate.isBlank())
                ? expectedDate
                : context.determineTargetDate(parsedOrderDate, diagnosticTest.getTurnaroundHours()).toString();

        // Link to the latest appointment or default reference
        long latestApptId = appointmentRepository.count();
        Long refId = latestApptId > 0 ? latestApptId : 101L;

        LabOrder order = new LabOrder(
                testCategory,
                finalDepartment,
                parsedOrderDate.toString(),
                finalExpectedDate,
                urgencyLevel,
                refId,
                "PENDING"
        );

        labOrderRepository.save(order);
        return "redirect:/lab-orders";
    }

    // UPDATE: Advance status (PENDING -> SAMPLE_COLLECTED -> COMPLETED)
    @PostMapping("/advance-status/{id}")
    public String advanceOrderStatus(@PathVariable Long id) {
        labOrderRepository.findById(id).ifPresent(order -> {
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("SAMPLE_COLLECTED");
            } else if ("SAMPLE_COLLECTED".equals(order.getStatus())) {
                order.setStatus("COMPLETED");
            }
            labOrderRepository.save(order);
        });
        return "redirect:/lab-orders";
    }

    // DELETE: Cancel / Revoke an order
    @PostMapping("/delete/{id}")
    public String deleteLabOrder(@PathVariable Long id) {
        labOrderRepository.deleteById(id);
        return "redirect:/lab-orders";
    }
}