package com.cuongnguyen.cse441.controller;

import com.cuongnguyen.cse441.dto.response.ApiResponse;
import com.cuongnguyen.cse441.dto.response.NotificationResponse;
import com.cuongnguyen.cse441.service.INotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cuongnguyen.cse441.constant.PathConstant.API_V1_NOTIFICATION;

@RestController
@RequestMapping(API_V1_NOTIFICATION)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    INotificationService notificationService;

    @GetMapping
    ApiResponse<List<NotificationResponse>> getAllNotificationsByUser() {
        return ApiResponse.success(notificationService.getNotificationsByUser());
    }

    @PutMapping("/{id}")
    public ApiResponse<NotificationResponse> markAsSeen(@PathVariable Long id) {
        return ApiResponse.success(notificationService.markAsSeen(id));
    }

}
