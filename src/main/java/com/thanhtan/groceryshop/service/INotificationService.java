package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.NotificationRequest;
import com.thanhtan.groceryshop.dto.response.NotificationResponse;
import com.thanhtan.groceryshop.entity.User;

import java.util.List;

public interface INotificationService {
    void sendNotificationToAdmin(User user, Long orderId);

    void sendNotificationToUser(NotificationRequest notificationRequest, List<Long> userIds);

    void sendUpdateOrderNotificationToUser(Long orderId, Long id);

    List<NotificationResponse> getNotificationsByUser();

    NotificationResponse markAsSeen(Long notificationId);


}
