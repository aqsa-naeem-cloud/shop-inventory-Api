package com.example.inventory.controller;
import com.example.inventory.dto.ProductResponse;
import com.example.inventory.dto.StockMovementRequest;
import com.example.inventory.dto.StockMovementResponse;
import com.example.inventory.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping("/{id}/stock-in")
    public ResponseEntity<ProductResponse> stockIn(
            @PathVariable Long id,
            @RequestBody StockMovementRequest request) {
        return ResponseEntity.ok(stockMovementService.stockIn(id, request));
    }

    @PostMapping("/{id}/stock-out")
    public ResponseEntity<ProductResponse> stockOut(
            @PathVariable Long id,
            @RequestBody StockMovementRequest request) {
        return ResponseEntity.ok(stockMovementService.stockOut(id, request));
    }

    @GetMapping("/{id}/movements")
    public ResponseEntity<List<StockMovementResponse>> getMovements(@PathVariable Long id) {
        return ResponseEntity.ok(stockMovementService.getProductMovements(id));
    }
}
