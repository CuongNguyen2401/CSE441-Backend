package com.cuongnguyen.cse441.repository;

import com.cuongnguyen.cse441.entity.Notification;
import com.cuongnguyen.cse441.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedDateDesc(User user);
}
