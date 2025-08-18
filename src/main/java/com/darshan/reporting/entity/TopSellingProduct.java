package com.darshan.reporting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSellingProduct {
    private Long productId;
    private String productName;
    private Integer totalQuantitySold;
}