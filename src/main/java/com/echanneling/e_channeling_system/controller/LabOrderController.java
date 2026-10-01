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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

    // CREATE: Place a new lab diagnostic order with Factory, Strategy, and validation
    @PostMapping("/create")
    public String createLabOrder(@RequestParam String testCategory,
                                 @RequestParam(required = false) String department,
                                 @RequestParam(required = false) String orderDate,
                                 @RequestParam(required = false) String expectedDate,
                                 @RequestParam String urgencyLevel,
                                 RedirectAttributes redirectAttributes) {

        // 1. Mandatory input guard
        if (testCategory == null || testCategory.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Diagnostic test category is mandatory.");
            return "redirect:/lab-orders";
        }

        // 2. Factory Pattern: Determine default department and standard turnaround
        DiagnosticTest diagnosticTest = DiagnosticTestFactory.createTest(testCategory);
        String finalDepartment = (department != null && !department.isBlank())
                ? department.trim()
                : diagnosticTest.getDefaultDepartment();

        // 3. Date validation & chronological check
        LocalDate parsedOrderDate;
        try {
            parsedOrderDate = (orderDate != null && !orderDate.isBlank())
                    ? LocalDate.parse(orderDate)
                    : LocalDate.now();
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Invalid order date format. Use YYYY-MM-DD.");
            return "redirect:/lab-orders";
        }

        // 4. Strategy Pattern: Resolve turnaround schedule based on urgency
        OrderProcessingContext context = new OrderProcessingContext();
        if ("STAT".equalsIgnoreCase(urgencyLevel) || "URGENT".equalsIgnoreCase(urgencyLevel)) {
            context.setStrategy(new StatUrgentPriorityStrategy());
        } else {
            context.setStrategy(new RoutinePriorityStrategy());
        }

        LocalDate parsedExpectedDate;
        try {
            parsedExpectedDate = (expectedDate != null && !expectedDate.isBlank())
                    ? LocalDate.parse(expectedDate)
                    : context.determineTargetDate(parsedOrderDate, diagnosticTest.getTurnaroundHours());
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Invalid expected completion date format.");
            return "redirect:/lab-orders";
        }

        // Clinical Rule: Completion date cannot precede the order date
        if (parsedExpectedDate.isBefore(parsedOrderDate)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Validation Error: Expected completion date cannot precede the order requisition date.");
            return "redirect:/lab-orders";
        }

        // Link to existing consultation appointment or default ID
        long latestApptId = appointmentRepository.count();
        Long refId = latestApptId > 0 ? latestApptId : 101L;

        // Persist validated order
        LabOrder order = new LabOrder(
                testCategory.trim(),
                finalDepartment,
                parsedOrderDate.toString(),
                parsedExpectedDate.toString(),
                urgencyLevel.toUpperCase().trim(),
                refId,
                "PENDING"
        );

        labOrderRepository.save(order);
        redirectAttributes.addFlashAttribute("successMessage", "Diagnostic order successfully validated and registered.");
        return "redirect:/lab-orders";
    }

    // UPDATE: Advance status (PENDING -> SAMPLE_COLLECTED -> COMPLETED)
    @PostMapping("/advance-status/{id}")
    public String advanceOrderStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        labOrderRepository.findById(id).ifPresent(order -> {
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("SAMPLE_COLLECTED");
            } else if ("SAMPLE_COLLECTED".equals(order.getStatus())) {
                order.setStatus("COMPLETED");
            }
            labOrderRepository.save(order);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Order lifecycle status updated successfully.");
        return "redirect:/lab-orders";
    }

    // DELETE: Cancel / Revoke an order
    @PostMapping("/delete/{id}")
    public String deleteLabOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        labOrderRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Diagnostic requisition voided successfully.");
        return "redirect:/lab-orders";
    }
}