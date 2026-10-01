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
import java.util.Set;

@Controller
@RequestMapping("/lab-orders")
public class LabOrderController {

    private final LabOrderRepository labOrderRepository;
    private final AppointmentRepository appointmentRepository;

    private static final Set<String> ALLOWED_URGENCIES = Set.of("ROUTINE", "URGENT", "STAT");

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

    // CREATE: Place a new lab diagnostic order with multi-tier validations
    @PostMapping("/create")
    public String createLabOrder(@RequestParam String testCategory,
                                 @RequestParam(required = false) String department,
                                 @RequestParam(required = false) String orderDate,
                                 @RequestParam(required = false) String expectedDate,
                                 @RequestParam String urgencyLevel,
                                 RedirectAttributes redirectAttributes) {

        // 1. Mandatory category validation
        if (testCategory == null || testCategory.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Clinical Error: A diagnostic test category must be chosen.");
            return "redirect:/lab-orders";
        }

        // 2. Urgency level validation
        String normalizedUrgency = urgencyLevel != null ? urgencyLevel.trim().toUpperCase() : "ROUTINE";
        if (!ALLOWED_URGENCIES.contains(normalizedUrgency)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Clinical Error: Invalid urgency level specified.");
            return "redirect:/lab-orders";
        }

        // 3. Factory Pattern: assign default department
        DiagnosticTest diagnosticTest = DiagnosticTestFactory.createTest(testCategory);
        String finalDepartment = (department != null && !department.isBlank())
                ? department.trim()
                : diagnosticTest.getDefaultDepartment();

        // 4. Order date parsing & past date validation
        LocalDate today = LocalDate.now();
        LocalDate parsedOrderDate;
        try {
            parsedOrderDate = (orderDate != null && !orderDate.isBlank())
                    ? LocalDate.parse(orderDate)
                    : today;
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Format Error: Order date must follow YYYY-MM-DD.");
            return "redirect:/lab-orders";
        }

        if (parsedOrderDate.isBefore(today.minusDays(1))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Clinical Guard: Order requisition date cannot be set in the past.");
            return "redirect:/lab-orders";
        }

        // 5. Strategy Pattern: Turnaround calculation
        OrderProcessingContext context = new OrderProcessingContext();
        if ("STAT".equals(normalizedUrgency) || "URGENT".equals(normalizedUrgency)) {
            context.setStrategy(new StatUrgentPriorityStrategy());
        } else {
            context.setStrategy(new RoutinePriorityStrategy());
        }

        // 6. Expected date validation & STAT urgency constraint
        LocalDate parsedExpectedDate;
        try {
            parsedExpectedDate = (expectedDate != null && !expectedDate.isBlank())
                    ? LocalDate.parse(expectedDate)
                    : context.determineTargetDate(parsedOrderDate, diagnosticTest.getTurnaroundHours());
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Format Error: Expected completion date is invalid.");
            return "redirect:/lab-orders";
        }

        // Chronological rule: Expected date cannot precede order date
        if (parsedExpectedDate.isBefore(parsedOrderDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chronological Error: Result date cannot precede the order requisition date.");
            return "redirect:/lab-orders";
        }

        // STAT clinical protocol rule: STAT orders must be finalized same-day or within 24 hours
        if ("STAT".equals(normalizedUrgency) && parsedExpectedDate.isAfter(parsedOrderDate.plusDays(1))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Clinical Protocol Alert: STAT (Emergency) orders cannot have a turnaround exceeding 24 hours.");
            return "redirect:/lab-orders";
        }

        // Link to active appointment reference
        long latestApptId = appointmentRepository.count();
        Long refId = latestApptId > 0 ? latestApptId : 101L;

        // Persist verified entity
        LabOrder order = new LabOrder(
                testCategory.trim(),
                finalDepartment,
                parsedOrderDate.toString(),
                parsedExpectedDate.toString(),
                normalizedUrgency,
                refId,
                "PENDING"
        );

        labOrderRepository.save(order);
        redirectAttributes.addFlashAttribute("successMessage", "Order #" + testCategory.trim() + " registered and verified successfully.");
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
        redirectAttributes.addFlashAttribute("successMessage", "Order lifecycle transitioned successfully.");
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