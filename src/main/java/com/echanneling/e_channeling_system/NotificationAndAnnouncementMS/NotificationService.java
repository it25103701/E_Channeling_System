package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final List<NotificationObserver> observers = new ArrayList<>();

    // Injects registered observers automatically via Spring Dependency Injection
    public NotificationService(NotificationRepository repository, List<NotificationObserver> registeredObservers) {
        this.repository = repository;
        this.observers.addAll(registeredObservers);
    }

    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(Notification notification) {
        for (NotificationObserver observer : observers) {
            observer.onNotificationCreated(notification);
        }
    }

    // CREATE: Save Notification & Notify Observers
    public Notification createNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification object cannot be null.");
        }
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Notification title cannot be empty or null.");
        }
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Notification message cannot be empty or null.");
        }

        Notification savedNotification = repository.save(notification);

        // OBSERVER PATTERN: Trigger all registered observers
        notifyObservers(savedNotification);

        return savedNotification;
    }

    // READ: View Notification Logs
    public List<Notification> getAllNotifications() {
        return repository.findAll();
    }

    public Notification getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE: Modify Notification
    public Notification updateNotification(Long id, Notification updated) {
        Notification existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));
        existing.setTitle(updated.getTitle());
        existing.setMessage(updated.getMessage());
        existing.setType(updated.getType());
        existing.setPreferenceRules(updated.getPreferenceRules());
        return repository.save(existing);
    }

    // DELETE: Clear Notification Log
    public void deleteNotification(Long id) {
        repository.deleteById(id);
    }
}