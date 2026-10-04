package com.echanneling.e_channeling_system.observer;

import com.echanneling.e_channeling_system.entity.Notification;

public interface NotificationObserver {
    void onNotificationCreated(Notification notification);
}