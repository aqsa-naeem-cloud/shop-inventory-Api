package com.example.inventory.dto;

public record StockMovementRequest(
        int quantity,
        String note
) {}
