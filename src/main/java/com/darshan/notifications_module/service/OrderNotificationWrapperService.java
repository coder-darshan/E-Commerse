package com.darshan.notifications_module.service;

import com.darshan.order_management.entity.Order;
import com.darshan.order_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderNotificationWrapperService {

    private final OrderService orderService;
    private final NotificationService notificationService;

    // Place order + notify customer
    public Order placeOrderWithNotification(Long cartId, String customerEmail, String customerPhone) {
        Order order = orderService.placeOrder(cartId);

        // Send notifications
        notificationService.notifyOrderPlaced(order, customerEmail, customerPhone);
        return order;
    }

    // Update order status + notify customer
    public Order updateOrderStatusAndNotify(Long orderId, String newStatus, String email, String phone) {
        Order order = orderService.updateOrderStatus(orderId, newStatus);

        // Send notifications
        notificationService.notifyOrderStatusChanged(order, email, phone);
        return order;
    }
}
