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

    // CREATE: Place a new lab diagnostic order with strict non-null validation
    @PostMapping("/create")
    public String createLabOrder(@RequestParam String testCategory,
                                 @RequestParam String department,
                                 @RequestParam String urgencyLevel,
                                 @RequestParam String orderDate,
                                 @RequestParam String expectedDate,
                                 RedirectAttributes redirectAttributes) {

        // 1. Mandatory Diagnostic Category Validation
        if (testCategory == null || testCategory.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Diagnostic test category cannot be left blank.");
            return "redirect:/lab-orders";
        }

        // 2. Mandatory Department Validation
        if (department == null || department.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Clinical department designation is mandatory.");
            return "redirect:/lab-orders";
        }

        // 3. Mandatory Urgency Level Validation
        String normalizedUrgency = urgencyLevel != null ? urgencyLevel.trim().toUpperCase() : "";
        if (!ALLOWED_URGENCIES.contains(normalizedUrgency)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Urgency level must be ROUTINE, URGENT, or STAT.");
            return "redirect:/lab-orders";
        }

        // 4. Mandatory Requisition Order Date Validation
        if (orderDate == null || orderDate.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Requisition order date is required.");
            return "redirect:/lab-orders";
        }

        LocalDate today = LocalDate.now();
        LocalDate parsedOrderDate;
        try {
            parsedOrderDate = LocalDate.parse(orderDate.trim());
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Invalid requisition date format (YYYY-MM-DD expected).");
            return "redirect:/lab-orders";
        }

        if (parsedOrderDate.isBefore(today)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Clinical Guard: Requisition date cannot be set in the past.");
            return "redirect:/lab-orders";
        }

        // 5. Mandatory Expected Delivery Date Validation
        if (expectedDate == null || expectedDate.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Expected result completion date is required.");
            return "redirect:/lab-orders";
        }

        LocalDate parsedExpectedDate;
        try {
            parsedExpectedDate = LocalDate.parse(expectedDate.trim());
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: Invalid expected completion date format.");
            return "redirect:/lab-orders";
        }

        // Chronological Sequence Check
        if (parsedExpectedDate.isBefore(parsedOrderDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chronological Error: Expected result date cannot precede the order date.");
            return "redirect:/lab-orders";
        }

        // STAT Urgency Protocol (Max 24h turnaround)
        if ("STAT".equals(normalizedUrgency) && parsedExpectedDate.isAfter(parsedOrderDate.plusDays(1))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Clinical Protocol Alert: STAT (Emergency) orders cannot exceed a 24-hour turnaround window.");
            return "redirect:/lab-orders";
        }

        // Factory & Strategy Pattern Integration
        DiagnosticTest diagnosticTest = DiagnosticTestFactory.createTest(testCategory.trim());
        OrderProcessingContext context = new OrderProcessingContext();
        if ("STAT".equals(normalizedUrgency) || "URGENT".equals(normalizedUrgency)) {
            context.setStrategy(new StatUrgentPriorityStrategy());
        } else {
            context.setStrategy(new RoutinePriorityStrategy());
        }

        // Link appointment reference
        long latestApptId = appointmentRepository.count();
        Long refId = latestApptId > 0 ? latestApptId : 101L;

        // Persist verified entity
        LabOrder order = new LabOrder(
                testCategory.trim(),
                department.trim(),
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