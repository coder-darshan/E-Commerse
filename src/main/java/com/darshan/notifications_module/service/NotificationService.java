package com.darshan.notifications_module.service;

import com.darshan.notifications_module.dto.NotificationDTO;
import com.darshan.notifications_module.entity.Notification;
import com.darshan.notifications_module.exception.ResourceNotFoundException;
import com.darshan.notifications_module.mapper.NotificationMapper;
import com.darshan.notifications_module.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = NotificationMapper.toEntity(dto);
        Notification saved = notificationRepository.save(notification);
        return NotificationMapper.toDTO(saved);
    }

    public List<NotificationDTO> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notification.setRead(true);
        return NotificationMapper.toDTO(notificationRepository.save(notification));
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }
}
