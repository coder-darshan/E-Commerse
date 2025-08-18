package com.darshan.order_management.service;

import com.darshan.cart.entity.Cart;
import com.darshan.cart.service.CartService;
import com.darshan.category_and_product.repository.ProductRepository;
import com.darshan.customer_management.entity.Customer;
import com.darshan.order_management.entity.Order;
import com.darshan.order_management.entity.OrderItem;
import com.darshan.order_management.entity.Payment;
import com.darshan.order_management.enums.OrderStatus;
import com.darshan.order_management.enums.PaymentStatus;
import com.darshan.order_management.repository.OrderRepository;
import com.darshan.order_management.repository.PaymentRepository;
import com.darshan.customer_management.repository.CustomerRepository;
import com.darshan.search_filter.entity.OrderSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    // ---------- ORDER PLACEMENT FROM CART ----------

    public Order placeOrder(Long cartId) {
        Cart cart = cartService.getCart(cartId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot place order.");
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());

            // Deduct stock
            if (cartItem.getProduct().getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + cartItem.getProduct().getName());
            }
            cartItem.getProduct().setStock(cartItem.getProduct().getStock() - cartItem.getQuantity());
            productRepository.save(cartItem.getProduct());

            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotalPrice(orderItems.stream().mapToDouble(OrderItem::getPrice).sum());

        return orderRepository.save(order);
    }

    // ---------- ORDER PLACEMENT FROM CUSTOMER + ITEMS ----------

    @Transactional
    public Order placeOrder(Long customerId, List<OrderItem> items) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        for (OrderItem item : items) {
            var product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            item.setOrder(order);
            item.setProduct(product);
            item.setPrice(product.getPrice() * item.getQuantity());

            order.getItems().add(item);
        }

        order.setTotalPrice(order.getItems().stream().mapToDouble(OrderItem::getPrice).sum());
        return orderRepository.save(order);
    }

    // ---------- CANCEL ORDER & RESTORE STOCK ----------

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        for (OrderItem item : order.getItems()) {
            var product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.delete(order);
    }

    // ---------- ORDER CRUD ----------

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order getOrderByIdOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // ---------- ORDER STATUS UPDATE USING ENUM ----------

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot change status after order is " + order.getStatus());
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // ---------- ORDER STATUS UPDATE USING STRING ----------

    @Transactional
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String currentStatus = order.getStatus().name();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Invalid status transition: " + currentStatus + " → " + newStatus);
        }

        try {
            OrderStatus newEnumStatus = OrderStatus.valueOf(newStatus.toUpperCase());
            order.setStatus(newEnumStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + newStatus);
        }

        return orderRepository.save(order);
    }

    private boolean isValidTransition(String currentStatus, String newStatus) {
        return switch (currentStatus) {
            case "PENDING" -> newStatus.equals("CONFIRMED") || newStatus.equals("CANCELLED");
            case "CONFIRMED" -> newStatus.equals("SHIPPED") || newStatus.equals("CANCELLED");
            case "SHIPPED" -> newStatus.equals("DELIVERED");
            case "DELIVERED", "CANCELLED" -> false;
            default -> false;
        };
    }

    // ---------- PAYMENT HANDLING ----------

    @Transactional
    public Payment makePayment(Long orderId, Payment paymentRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Payment can only be made on PENDING orders");
        }

        paymentRequest.setOrder(order);
        paymentRequest.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(paymentRequest);

        if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
        }

        return savedPayment;
    }
    public Page<Order> searchOrders(Long customerId, String status,
                                    java.time.LocalDateTime startDate, java.time.LocalDateTime endDate,
                                    int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return orderRepository.findAll(OrderSpecification.getFilter(customerId, status, startDate, endDate), pageRequest);
    }
}
