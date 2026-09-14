package com.echanneling.e_channeling_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping
    public String viewDashboard(Model model) {
        model.addAttribute("reports", reportRepository.findAll());
        model.addAttribute("newReport", new AnalyticsReport());
        return "reports";
    }

    @PostMapping("/generate")
    public String generateReport(@ModelAttribute AnalyticsReport report) {
        Random rand = new Random();
        report.setTotalAppointments(50 + rand.nextInt(150));
        report.setTotalRevenue(report.getTotalAppointments() * 2500.0);
        report.setSatisfactionRate(80.0 + (rand.nextDouble() * 18.0));
        report.setStatus("Active");

        reportRepository.save(report);
        return "redirect:/reports";
    }

    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportRepository.deleteById(id);
        return "redirect:/reports";
    }
}