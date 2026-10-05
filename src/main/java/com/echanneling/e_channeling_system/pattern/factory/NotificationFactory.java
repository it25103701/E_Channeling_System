package com.echanneling.e_channeling_system.Observer;

import com.echanneling.e_channeling_system.entity.Notification;

public class NotificationFactory {

    private NotificationFactory() {
    }

    // Factory method for creating direct recipient notifications
    public static Notification createDirectNotification(String title, String message, String type, String patientId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRecipientType("DIRECT");
        notification.setPatientId(patientId);
        notification.setPreferenceRules("Target Patient: " + patientId);
        return notification;
    }

    // Factory method for creating broadcast announcements
    public static Notification createBroadcastNotification(String title, String message, String type, String preferenceRules) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRecipientType("BROADCAST");
        notification.setPreferenceRules(preferenceRules);
        return notification;
    }
}