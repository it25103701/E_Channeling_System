package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener implements NotificationObserver {

    @Override
    public void onNotificationCreated(Notification notification) {
        // Concrete observer action when a notification or announcement is saved
        System.out.println("[Observer Action] New notification registered: " + notification.getTitle());
    }
}