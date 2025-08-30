package com.darshan.notifications_module.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    @NotNull(message = "Notification type is required")
    @Column(name = "notification_type")
    private String type;

    @Column(name = "is_read")
    private boolean read;

    private String createdAt;
}
