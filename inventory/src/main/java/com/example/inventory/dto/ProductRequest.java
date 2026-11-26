package com.example.inventory.dto;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        String sku,
        BigDecimal price,
        int minStock,
        Long categoryId
) {}









