package com.echanneling.e_channeling_system.controller;

import com.echanneling.e_channeling_system.entity.Notification;
import com.echanneling.e_channeling_system.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    // CREATE NOTIFICATION (POST /notifications/send)
    // =========================================
    @PostMapping("/send")
    public String sendNotification(@ModelAttribute("notification") Notification notification) {
        notificationService.createNotification(notification);
        return "redirect:/notifications";
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