package com.darshan.notifications_module.controller;

import com.darshan.notifications_module.service.OrderNotificationWrapperService;
import com.darshan.order_management.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final OrderNotificationWrapperService orderNotificationWrapperService;

    // Place order + notify
    @PostMapping("/order/place")
    public ResponseEntity<Order> placeOrder(
            @RequestParam Long cartId,
            @RequestParam String email,
            @RequestParam String phone
    ) {
        Order order = orderNotificationWrapperService.placeOrderWithNotification(cartId, email, phone);
        return ResponseEntity.ok(order);
    }

    // Update order status + notify
    @PostMapping("/order/{orderId}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam String email,
            @RequestParam String phone
    ) {
        Order order = orderNotificationWrapperService.updateOrderStatusAndNotify(orderId, status, email, phone);
        return ResponseEntity.ok(order);
    }
}
