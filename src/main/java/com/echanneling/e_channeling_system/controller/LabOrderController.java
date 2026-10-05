package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.LabOrder;
import com.echanneling.e_channeling_system.pattern.factory.DiagnosticTest;
import com.echanneling.e_channeling_system.pattern.factory.DiagnosticTestFactory;
import com.echanneling.e_channeling_system.pattern.strategy.OrderProcessingContext;
import com.echanneling.e_channeling_system.pattern.strategy.RoutinePriorityStrategy;
import com.echanneling.e_channeling_system.pattern.strategy.StatUrgentPriorityStrategy;
import com.echanneling.e_channeling_system.repository.AppointmentRepository;
import com.echanneling.e_channeling_system.service.LabOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/lab-orders")
public class LabOrderController {

    private final LabOrderService labOrderService;
    private final AppointmentRepository appointmentRepository;

    private static final Set<String> ALLOWED_URGENCIES = Set.of("ROUTINE", "URGENT", "STAT");

    public LabOrderController(LabOrderService labOrderService,
                              AppointmentRepository appointmentRepository) {
        this.labOrderService = labOrderService;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    public String getAllLabOrders(Model model) {
        model.addAttribute("labOrders", labOrderService.getAllLabOrders());
        model.addAttribute("appointmentCount", appointmentRepository.count());
        model.addAttribute("editingOrder", null);
        return "lab-orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<LabOrder> orderOpt = labOrderService.getLabOrderById(id);
        if (orderOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Lab order #LAB-" + id + " not found.");
            return "redirect:/lab-orders";
        }

        model.addAttribute("labOrders", labOrderService.getAllLabOrders());
        model.addAttribute("appointmentCount", appointmentRepository.count());
        model.addAttribute("editingOrder", orderOpt.get());
        return "lab-orders";
    }

    @PostMapping("/create")
    public String createLabOrder(@RequestParam String testCategory,
                                 @RequestParam String department,
                                 @RequestParam String urgencyLevel,
                                 @RequestParam String orderDate,
                                 @RequestParam String expectedDate,
                                 RedirectAttributes redirectAttributes) {

        String validationError = validateOrderPayload(testCategory, department, urgencyLevel, orderDate, expectedDate, true);
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("errorMessage", validationError);
            return "redirect:/lab-orders";
        }

        LocalDate parsedOrderDate = LocalDate.parse(orderDate.trim());
        LocalDate parsedExpectedDate = LocalDate.parse(expectedDate.trim());
        String normalizedUrgency = urgencyLevel.trim().toUpperCase();

        // Factory & Strategy Pattern Integration
        DiagnosticTest diagnosticTest = DiagnosticTestFactory.createTest(testCategory.trim());
        OrderProcessingContext context = new OrderProcessingContext();
        if ("STAT".equals(normalizedUrgency) || "URGENT".equals(normalizedUrgency)) {
            context.setStrategy(new StatUrgentPriorityStrategy());
        } else {
            context.setStrategy(new RoutinePriorityStrategy());
        }

        long latestApptId = appointmentRepository.count();
        Long refId = latestApptId > 0 ? latestApptId : 101L;

        LabOrder order = new LabOrder(
                testCategory.trim(),
                department.trim(),
                parsedOrderDate.toString(),
                parsedExpectedDate.toString(),
                normalizedUrgency,
                refId,
                "PENDING"
        );

        labOrderService.saveLabOrder(order);
        redirectAttributes.addFlashAttribute("successMessage", "Order #" + testCategory.trim() + " registered and verified successfully.");
        return "redirect:/lab-orders";
    }

    @PostMapping("/update/{id}")
    public String updateLabOrder(@PathVariable Long id,
                                 @RequestParam String testCategory,
                                 @RequestParam String department,
                                 @RequestParam String urgencyLevel,
                                 @RequestParam String orderDate,
                                 @RequestParam String expectedDate,
                                 RedirectAttributes redirectAttributes) {

        Optional<LabOrder> orderOpt = labOrderService.getLabOrderById(id);
        if (orderOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Lab order #LAB-" + id + " does not exist.");
            return "redirect:/lab-orders";
        }

        String validationError = validateOrderPayload(testCategory, department, urgencyLevel, orderDate, expectedDate, false);
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("errorMessage", validationError);
            return "redirect:/lab-orders/edit/" + id;
        }

        LocalDate parsedOrderDate = LocalDate.parse(orderDate.trim());
        LocalDate parsedExpectedDate = LocalDate.parse(expectedDate.trim());
        String normalizedUrgency = urgencyLevel.trim().toUpperCase();

        LabOrder order = orderOpt.get();
        order.setTestCategory(testCategory.trim());
        order.setDepartment(department.trim());
        order.setUrgencyLevel(normalizedUrgency);
        order.setOrderDate(parsedOrderDate.toString());
        order.setExpectedDate(parsedExpectedDate.toString());

        labOrderService.saveLabOrder(order);
        redirectAttributes.addFlashAttribute("successMessage", "Lab order #LAB-" + id + " updated successfully.");
        return "redirect:/lab-orders";
    }

    @PostMapping("/advance-status/{id}")
    public String advanceOrderStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        labOrderService.getLabOrderById(id).ifPresent(order -> {
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("SAMPLE_COLLECTED");
            } else if ("SAMPLE_COLLECTED".equals(order.getStatus())) {
                order.setStatus("COMPLETED");
            }
            labOrderService.saveLabOrder(order);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Order lifecycle transitioned successfully.");
        return "redirect:/lab-orders";
    }

    @PostMapping("/delete/{id}")
    public String deleteLabOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        labOrderService.deleteLabOrder(id);
        redirectAttributes.addFlashAttribute("successMessage", "Diagnostic requisition voided successfully.");
        return "redirect:/lab-orders";
    }

    private String validateOrderPayload(String testCategory,
                                        String department,
                                        String urgencyLevel,
                                        String orderDate,
                                        String expectedDate,
                                        boolean checkPastDate) {
        if (testCategory == null || testCategory.trim().isEmpty()) {
            return "Validation Error: Diagnostic test category cannot be left blank.";
        }
        if (department == null || department.trim().isEmpty()) {
            return "Validation Error: Clinical department designation is mandatory.";
        }

        String normalizedUrgency = urgencyLevel != null ? urgencyLevel.trim().toUpperCase() : "";
        if (!ALLOWED_URGENCIES.contains(normalizedUrgency)) {
            return "Validation Error: Urgency level must be ROUTINE, URGENT, or STAT.";
        }

        if (orderDate == null || orderDate.trim().isEmpty()) {
            return "Validation Error: Requisition order date is required.";
        }

        LocalDate parsedOrderDate;
        try {
            parsedOrderDate = LocalDate.parse(orderDate.trim());
        } catch (DateTimeParseException e) {
            return "Validation Error: Invalid requisition date format (YYYY-MM-DD expected).";
        }

        if (checkPastDate && parsedOrderDate.isBefore(LocalDate.now())) {
            return "Clinical Guard: Requisition date cannot be set in the past.";
        }

        if (expectedDate == null || expectedDate.trim().isEmpty()) {
            return "Validation Error: Expected result completion date is required.";
        }

        LocalDate parsedExpectedDate;
        try {
            parsedExpectedDate = LocalDate.parse(expectedDate.trim());
        } catch (DateTimeParseException e) {
            return "Validation Error: Invalid expected completion date format.";
        }

        if (parsedExpectedDate.isBefore(parsedOrderDate)) {
            return "Chronological Error: Expected result date cannot precede the order date.";
        }

        if ("STAT".equals(normalizedUrgency) && parsedExpectedDate.isAfter(parsedOrderDate.plusDays(1))) {
            return "Clinical Protocol Alert: STAT (Emergency) orders cannot exceed a 24-hour turnaround window.";
        }

        return null;
    }
}