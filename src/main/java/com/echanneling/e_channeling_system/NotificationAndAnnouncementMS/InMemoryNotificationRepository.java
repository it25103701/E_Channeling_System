package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryNotificationRepository implements NotificationRepository {
    private final Map<Long, Notification> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(idGenerator.getAndIncrement());
        }
        store.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}