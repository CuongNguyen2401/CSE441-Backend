package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.request.NotificationRequest;
import com.thanhtan.groceryshop.dto.response.NotificationResponse;
import com.thanhtan.groceryshop.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toNotificationResponse(Notification notification);

    Notification toNotification(NotificationRequest notificationRequest);

    List<NotificationResponse> toNotificationResponseList(List<Notification> notifications);



}
