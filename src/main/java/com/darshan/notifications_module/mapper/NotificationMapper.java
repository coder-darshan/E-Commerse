package com.darshan.notifications_module.mapper;


import com.darshan.notifications_module.dto.NotificationDTO;
import com.darshan.notifications_module.entity.Notification;
import com.darshan.notifications_module.enums.NotificationType;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification entity) {
        if (entity == null) return null;

        return NotificationDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .message(entity.getMessage())
                .type(entity.getType().name())
                .read(entity.isRead())
                .createdAt(entity.getCreatedAt().toString())
                .build();
    }

    public static Notification toEntity(NotificationDTO dto) {
        if (dto == null) return null;

        return Notification.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .message(dto.getMessage())
                .type(Enum.valueOf(NotificationType.class, dto.getType()))
                .read(dto.isRead())
                .build();
    }
}
