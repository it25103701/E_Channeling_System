package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> findAll();
    Optional<Notification> findById(Long id);
    void deleteById(Long id);
}
