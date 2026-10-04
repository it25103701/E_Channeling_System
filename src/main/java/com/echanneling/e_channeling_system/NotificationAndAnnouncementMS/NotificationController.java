package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @Controller tells Spring that this class handles web requests
@Controller
@RequestMapping("/notifications")
public class NotificationController {


    // Reference to the Service layer
    // The controller uses this service to perform notification operations
    private final NotificationService notificationService;

    // Constructor Injection
    // Spring automatically provides the NotificationService object
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 1. Read: Display Notifications Table & Form
    @GetMapping
    public String showNotificationsPage(Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        model.addAttribute("notification", new Notification());
        return "notifications";
    }

    // 2. Create / Update: Process Form Submission
    @PostMapping("/save")
    public String saveNotification(@ModelAttribute("notification") Notification notification) {
        // Check whether an ID already exists
        // ID exists → update existing notification
        // ID is null → create a new notification

        if (notification.getId() != null) {
            // Update existing record
            notificationService.updateNotification(notification.getId(), notification);
        } else {
            // Create new record(s)
            // Check whether the notification is DIRECT
            // and a valid Patient ID was provided

            if ("DIRECT".equals(notification.getRecipientType())
                    && notification.getPatientId() != null
                    && !notification.getPatientId().trim().isEmpty()) {

                // Split multiple comma-separated Patient IDs (e.g., "P-101, P-102, P-103")
                String[] patientIds = notification.getPatientId().split(",");

                for (String pid : patientIds) {
                    String cleanPid = pid.trim();  // trim() removes unnecessary spaces
                    if (!cleanPid.isEmpty()) { // Ignore empty Patient IDs
                        // FACTORY PATTERN: Instantiate direct notification via factory method
                        // Factory creates a direct notification object

                        Notification singleNotif = NotificationFactory.createDirectNotification(
                                notification.getTitle(),
                                notification.getMessage(),
                                notification.getType(),
                                cleanPid
                        );
                        // Save the newly created notification
                        notificationService.createNotification(singleNotif);
                    }
                }
            } else {
                // FACTORY PATTERN: Instantiate broadcast notification via factory method
                // Factory creates a broadcast notification object

                Notification broadcastNotif = NotificationFactory.createBroadcastNotification(
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getType(),
                        notification.getPreferenceRules()
                );
                // Save the broadcast notification
                notificationService.createNotification(broadcastNotif);
            }
        }
        return "redirect:/notifications";
    }

    // 3. Edit: Populate Form with Existing   Data
    @GetMapping("/edit/{id}")
    public String editNotification(@PathVariable Long id, Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        model.addAttribute("notification", notificationService.getById(id));
        return "notifications";
    }

    // 4. Delete: Remove Record
    @GetMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        // Deletes the notification with the specified ID
        notificationService.deleteNotification(id);

        // Redirects back to the notifications page
        // so the updated list can be displayed
        return "redirect:/notifications";
    }
}