package com.example.inventory.dto;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        String name,
        BigDecimal price,
        int minStock
) {}
