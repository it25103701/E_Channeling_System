package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.InsuranceClaim;
import com.echanneling.e_channeling_system.service.InsuranceClaimService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/insurance")
public class InsuranceClaimController {

    private final InsuranceClaimService claimService;

    public InsuranceClaimController(InsuranceClaimService claimService) {
        this.claimService = claimService;
    }

    // Default route: visiting http://localhost:8080/insurance opens the admin claims dashboard
    @GetMapping
    public String index() {
        return "redirect:/insurance/admin/claims";
    }

    // Patient: show the claim form
    @GetMapping("/claim/new")
    public String showClaimForm(Model model) {
        model.addAttribute("claim", new InsuranceClaim());
        return "insurance-claim-form";
    }

    // Patient: submit the form
    @PostMapping("/claim")
    public String submitClaim(@ModelAttribute("claim") InsuranceClaim claim) {
        InsuranceClaim saved = claimService.submitClaim(claim);
        return "redirect:/insurance/claim/" + saved.getId();
    }

    // Patient: see claim status
    @GetMapping("/claim/{id}")
    public String viewClaim(@PathVariable Long id, Model model) {
        model.addAttribute("claim", claimService.getClaimById(id));
        return "insurance-claim-status";
    }

    // Financial Admin: list all claims
    @GetMapping("/admin/claims")
    public String adminClaims(Model model) {
        model.addAttribute("claims", claimService.getAllClaims());
        return "insurance-admin-claims";
    }

    // Financial Admin: approve or adjust coverage
    @PostMapping("/admin/claims/{id}/approve")
    public String approve(@PathVariable Long id, @RequestParam double coveragePercentage) {
        claimService.approveClaim(id, coveragePercentage);
        return "redirect:/insurance/admin/claims";
    }

    // Financial Admin: reject with a reason
    @PostMapping("/admin/claims/{id}/reject")
    public String reject(@PathVariable Long id, @RequestParam String reason) {
        claimService.rejectClaim(id, reason);
        return "redirect:/insurance/admin/claims";
    }

    // Cancel with a reason
    @PostMapping("/admin/claims/{id}/cancel")
    public String cancel(@PathVariable Long id, @RequestParam String reason) {
        claimService.cancelClaim(id, reason);
        return "redirect:/insurance/admin/claims";
    }

    // Financial Admin: view the audit log
    @GetMapping("/admin/audit-log")
    public String auditLog(Model model) {
        model.addAttribute("logs", claimService.getAuditLogs());
        return "insurance-audit-log";
    }

    @PostMapping("/admin/claims/{id}/delete")
    public String delete(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return "redirect:/insurance/admin/claims";
    }
}