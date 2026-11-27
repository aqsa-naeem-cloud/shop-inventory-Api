package com.example.inventory.service;
import com.example.inventory.dto.ProductRequest;
import com.example.inventory.dto.ProductResponse;
import com.example.inventory.dto.ProductUpdateRequest;
import com.example.inventory.entity.Category;
import com.example.inventory.entity.Product;
import com.example.inventory.exception.DuplicateResourceException;
import com.example.inventory.exception.ProductNotFoundException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.CategoryRepository;
import com.example.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public ProductResponse createProduct(ProductRequest request) {
        // BUSINESS RULE: SKU must be unique
        if (productRepository.existsBySku(request.sku())) {
            throw new DuplicateResourceException("Product with SKU '" + request.sku() + "' already exists");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.categoryId()));

        Product product = new Product();
        product.setName(request.name());
        product.setSku(request.sku());
        product.setPrice(request.price());
        product.setQuantity(0); // BUSINESS RULE: starts at 0
        product.setMinStock(request.minStock());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }


    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }


    public ProductResponse getProductById(Long id) {
        // BUSINESS RULE: Product not found -> throw ProductNotFoundException
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    // 4. Update Product (ONLY name, price, minStock)
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        // BUSINESS RULE: Product not found -> throw ProductNotFoundException
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));


        product.setName(request.name());
        product.setPrice(request.price());
        product.setMinStock(request.minStock());

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    public void deleteProduct(Long id) {
        // BUSINESS RULE: Product not found -> throw ProductNotFoundException
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }


    public List<ProductResponse> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private ProductResponse mapToResponse(Product product) {
        boolean isLowStock = product.getQuantity() < product.getMinStock();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory().getName(),
                isLowStock
        );
    }
}

