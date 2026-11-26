package com.example.inventory.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        BigDecimal price,
        int quantity,
        String categoryName,
        boolean lowStock
) {}
