package com.example.inventory.dto;

import com.example.inventory.entity.MovementType;
import java.time.LocalDateTime;

public record StockMovementResponse(
        Long id,
        Long productId,
        String productName,
        MovementType type,
        int quantity,
        String note,
        LocalDateTime createdAt
) {}

