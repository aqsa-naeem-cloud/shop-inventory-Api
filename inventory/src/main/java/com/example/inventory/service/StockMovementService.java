package com.example.inventory.service;
import com.example.inventory.dto.ProductResponse;
import com.example.inventory.dto.StockMovementRequest;
import com.example.inventory.dto.StockMovementResponse;
import com.example.inventory.entity.MovementType;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.StockMovement;
import com.example.inventory.exception.InsufficientStockException;
import com.example.inventory.exception.ProductNotFoundException;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    @Transactional
    public ProductResponse stockIn(Long productId, StockMovementRequest request) {
        validateQuantity(request.quantity());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        product.setQuantity(product.getQuantity() + request.quantity());
        Product updatedProduct = productRepository.save(product);

        StockMovement movement =  new StockMovement();
        movement.setProduct(updatedProduct);
        movement.setType(MovementType.IN);
        movement.setQuantity(request.quantity());
        movement.setNote(request.note());
        stockMovementRepository.save(movement);

        return mapToProductResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse stockOut(Long productId, StockMovementRequest request) {
        validateQuantity(request.quantity());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        if (product.getQuantity() < request.quantity()) {
            throw new InsufficientStockException(
                    "Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + request.quantity()
            );
        }

        product.setQuantity(product.getQuantity() - request.quantity());
        Product updatedProduct = productRepository.save(product);

        StockMovement movement = new StockMovement();
        movement.setProduct(updatedProduct);
        movement.setType(MovementType.OUT);
        movement.setQuantity(request.quantity());
        movement.setNote(request.note());
        stockMovementRepository.save(movement);


        return mapToProductResponse(updatedProduct);
    }

    public List<StockMovementResponse> getProductMovements(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }

        return stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(this::mapToMovementResponse)
                .toList();
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getQuantity() < product.getMinStock()
        );
    }

    private StockMovementResponse mapToMovementResponse(StockMovement movement) {
        return new StockMovementResponse(
                movement.getId(),
                movement.getProduct().getId(),
                movement.getProduct().getName(),
                movement.getType(),
                movement.getQuantity(),
                movement.getNote(),
                movement.getCreatedAt()
        );
    }
}
