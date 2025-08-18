package com.darshan.notifications_module.service;

import com.darshan.order_management.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    // Twilio sandbox credentials (replace with your sandbox keys)
    private final String ACCOUNT_SID = "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private final String AUTH_TOKEN = "your_auth_token";
    private final String FROM_PHONE = "+1234567890";  // Twilio sandbox number

    // Send email notification
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }

    // Send SMS notification (Twilio sandbox)
    public void sendSMS(String to, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(new PhoneNumber(to), new PhoneNumber(FROM_PHONE), message).create();
    }

    // Notify customer on order placement
    public void notifyOrderPlaced(Order order, String email, String phone) {
        String subject = "Order Placed Successfully! Order ID: " + order.getId();
        String body = "Dear Customer,\n\nYour order has been placed successfully.\nTotal Amount: " + order.getTotalPrice() +
                "\nStatus: " + order.getStatus() + "\n\nThank you for shopping with us!";
        sendEmail(email, subject, body);
        sendSMS(phone, "Order Placed: ID " + order.getId() + ", Amount " + order.getTotalPrice());
    }

    // Notify customer on order status change
    public void notifyOrderStatusChanged(Order order, String email, String phone) {
        String subject = "Order Status Updated! Order ID: " + order.getId();
        String body = "Dear Customer,\n\nYour order status has been updated to: " + order.getStatus() +
                "\n\nThank you for shopping with us!";
        sendEmail(email, subject, body);
        sendSMS(phone, "Order Status Updated: ID " + order.getId() + ", Status " + order.getStatus());
    }
}
