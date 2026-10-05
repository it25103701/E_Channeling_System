package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 1. Read: Display Notifications Table & Empty Form
    @GetMapping
    public String showNotificationsPage(Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        model.addAttribute("notification", new Notification());
        return "notifications";
    }

    // 2. Create / Update: Process Form Submission
    @PostMapping("/save")
    public String saveNotification(@ModelAttribute("notification") Notification notification) {
        if (notification.getId() != null) {
            notificationService.updateNotification(notification.getId(), notification);
        } else {
            notificationService.createNotification(notification);
        }
        return "redirect:/notifications";
    }

    // 3. Edit: Populate Form with Existing Record Data
    @GetMapping("/edit/{id}")
    public String editNotification(@PathVariable Long id, Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        model.addAttribute("notification", notificationService.getById(id));
        return "notifications";
    }

    // 4. Delete: Remove Record and Refresh View
    @GetMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }
}