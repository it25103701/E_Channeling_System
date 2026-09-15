package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.AnalyticsReport;
import com.echanneling.e_channeling_system.repository.AppointmentRepository;
import com.echanneling.e_channeling_system.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired(required = false)
    private AppointmentRepository appointmentRepository;

    @GetMapping
    public String viewReports(Model model) {
        List<AnalyticsReport> reports = reportRepository.findAll();
        model.addAttribute("reports", reports);

        // Bindings required by reports.html form tags:
        model.addAttribute("newReport", new AnalyticsReport());
        model.addAttribute("report", new AnalyticsReport());
        model.addAttribute("analyticsReport", new AnalyticsReport());

        return "reports";
    }

    @PostMapping("/generate")
    public String generateReport(
            @RequestParam(value = "category", required = false, defaultValue = "Operational / Bookings") String category,
            @RequestParam(value = "department", required = false, defaultValue = "All Departments") String department,
            @RequestParam(value = "startDate", required = false, defaultValue = "2026-09-01") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "2026-09-15") String endDate) {

        long apptCount = 0;
        if (appointmentRepository != null) {
            try {
                apptCount = appointmentRepository.count();
            } catch (Exception ignored) {
                apptCount = 0;
            }
        }

        AnalyticsReport report = new AnalyticsReport();
        report.setReportType(category);
        report.setDepartment(department);
        report.setDateRange(startDate + " to " + endDate);
        report.setTotalAppointments((int) apptCount);
        report.setTotalRevenue(apptCount * 2500.0);
        report.setSatisfactionRate(apptCount > 0 ? 95.5 : 0.0);

        reportRepository.save(report);

        return "redirect:/reports";
    }

    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable("id") Long id) {
        reportRepository.deleteById(id);
        return "redirect:/reports";
    }
}