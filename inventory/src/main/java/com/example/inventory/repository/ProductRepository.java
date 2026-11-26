package com.example.inventory.repository;

import com.example.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Derived query 1: Check unique SKU
    boolean existsBySku(String sku);

    // Derived query 2: Search by name (ignore case)
    List<Product> findByNameContainingIgnoreCase(String name);

    // JPQL Query: Products where quantity < minStock
    @Query("SELECT p FROM Product p WHERE p.quantity < p.minStock")
    List<Product> findLowStockProducts();
}
