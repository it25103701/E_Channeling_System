package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository   //Marks NotificationRepository as a Spring Repository component.
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    //An interface specifies what functionality is available without manually implementing the methods.

}
