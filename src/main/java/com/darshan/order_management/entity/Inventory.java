package com.darshan.order_management.entity;

import com.darshan.category_and_product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer availableStock;

    private Integer reservedStock; // for reserved quantity during order processing
}
