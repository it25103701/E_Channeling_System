package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.Notification;
import com.echanneling.e_channeling_system.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller managing notification broadcasts and direct communications.
 * Provides endpoints to list, create, update, and remove notifications.
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // =========================================
    // VIEW ALL NOTIFICATIONS (GET /notifications)
    // =========================================
    @GetMapping
    public String showNotificationsPage(Model model) {
        List<Notification> notifications = notificationService.getAllNotifications();
        model.addAttribute("notifications", notifications);
        model.addAttribute("notification", new Notification());
        return "notifications";
    }

    // =========================================
    // CREATE / SAVE NOTIFICATION (POST /notifications/save, /notifications/send)
    // =========================================
    @PostMapping({"/save", "/send"})
    public String saveNotification(@ModelAttribute("notification") Notification notification) {
        notificationService.createNotification(notification);
        return "redirect:/notifications";
    }

    // =========================================
    // EDIT NOTIFICATION VIEW (GET /notifications/edit/{id})
    // =========================================
    @GetMapping("/edit/{id}")
    public String editNotification(@PathVariable("id") Long id, Model model) {
        List<Notification> notifications = notificationService.getAllNotifications();
        model.addAttribute("notifications", notifications);

        // Find existing record and bind to form, otherwise fallback to empty entity
        Notification target = notifications.stream()
                .filter(n -> n.getId() != null && n.getId().equals(id))
                .findFirst()
                .orElse(new Notification());

        model.addAttribute("notification", target);
        return "notifications";
    }

    // =========================================
    // DELETE NOTIFICATION (GET /notifications/delete/{id})
    // =========================================
    @GetMapping("/delete/{id}")
    public String deleteNotification(@PathVariable("id") Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }
}