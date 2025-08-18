package com.darshan.order_management.service;

import com.darshan.order_management.entity.Inventory;
import com.darshan.order_management.entity.Order;
import com.darshan.order_management.entity.OrderItem;
import com.darshan.order_management.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // -------------------- Per-Product Operations --------------------

    public boolean checkStock(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No inventory found for product"));
        return inventory.getAvailableStock() >= quantity;
    }

    public void reduceStock(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No inventory found for product"));
        if (inventory.getAvailableStock() < quantity) {
            throw new RuntimeException("Not enough stock for product " + productId);
        }
        inventory.setAvailableStock(inventory.getAvailableStock() - quantity);
        inventoryRepository.save(inventory);
    }

    public void restoreStock(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No inventory found for product"));
        inventory.setAvailableStock(inventory.getAvailableStock() + quantity);
        inventoryRepository.save(inventory);
    }

    // -------------------- Order-Based Stock Handling --------------------

    @Transactional
    public boolean reserveStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("No inventory found for product"));

            if (inventory.getAvailableStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + item.getProduct().getName());
            }

            inventory.setAvailableStock(inventory.getAvailableStock() - item.getQuantity());
            inventory.setReservedStock(inventory.getReservedStock() + item.getQuantity());
            inventoryRepository.save(inventory);
        }
        return true;
    }

    @Transactional
    public void releaseStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("No inventory found for product"));

            inventory.setAvailableStock(inventory.getAvailableStock() + item.getQuantity());
            inventory.setReservedStock(inventory.getReservedStock() - item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    @Transactional
    public void confirmStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("No inventory found for product"));

            inventory.setReservedStock(inventory.getReservedStock() - item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }
}
