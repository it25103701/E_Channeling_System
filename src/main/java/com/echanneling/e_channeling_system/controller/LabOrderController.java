package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.LabOrder;
import com.echanneling.e_channeling_system.repository.AppointmentRepository;
import com.echanneling.e_channeling_system.repository.LabOrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // CREATE: Place a new lab diagnostic order
    @PostMapping("/create")
    public String createLabOrder(@RequestParam String testCategory,
                                 @RequestParam String department,
                                 @RequestParam String orderDate,
                                 @RequestParam String expectedDate,
                                 @RequestParam String urgencyLevel) {

        // Link to the latest appointment or default reference
        long latestApptId = appointmentRepository.count();
        Long refId = latestApptId > 0 ? latestApptId : 101L;

        LabOrder order = new LabOrder(
                testCategory,
                department,
                orderDate,
                expectedDate,
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