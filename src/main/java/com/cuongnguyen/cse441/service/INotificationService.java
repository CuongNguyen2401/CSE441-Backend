package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.NotificationRequest;
import com.cuongnguyen.cse441.dto.response.NotificationResponse;
import com.cuongnguyen.cse441.entity.User;

import java.util.List;

public interface INotificationService {
    void sendNotificationToAdmin(User user, Long orderId);

    void sendNotificationToUser(NotificationRequest notificationRequest, List<Long> userIds);

    void sendUpdateOrderNotificationToUser(Long orderId, Long id);

    List<NotificationResponse> getNotificationsByUser();

    NotificationResponse markAsSeen(Long notificationId);


}
