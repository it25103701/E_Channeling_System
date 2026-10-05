package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    // CREATE: Draft & Broadcast Announcements
    public Notification createNotification(Notification notification) {
        return repository.save(notification);
    }

    // READ: View Notification Logs & Alert Histories
    public List<Notification> getAllNotifications() {
        return repository.findAll();
    }

    public Notification getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE: Modify Notification Preference Rules & Alert Templates
    public Notification updateNotification(Long id, Notification updated) {
        Notification existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));
        existing.setTitle(updated.getTitle());
        existing.setMessage(updated.getMessage());
        existing.setType(updated.getType());
        existing.setPreferenceRules(updated.getPreferenceRules());
        return repository.save(existing);
    }

    // DELETE: Clear Notification Logs & Dismiss Active Alerts
    public void deleteNotification(Long id) {
        repository.deleteById(id);
    }
}