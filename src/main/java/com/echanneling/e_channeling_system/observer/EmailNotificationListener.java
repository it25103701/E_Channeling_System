package com.echanneling.e_channeling_system.observer;

import com.echanneling.e_channeling_system.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener implements NotificationObserver {

    @Override
    public void onNotificationCreated(Notification notification) {
        // Concrete observer action when a notification or announcement is saved
        System.out.println("[Observer Action] New notification registered: " + notification.getTitle());
    }
}