package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.NotificationRequest;
import com.cuongnguyen.cse441.dto.response.NotificationResponse;
import com.cuongnguyen.cse441.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toNotificationResponse(Notification notification);

    Notification toNotification(NotificationRequest notificationRequest);

    List<NotificationResponse> toNotificationResponseList(List<Notification> notifications);



}
