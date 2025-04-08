package com.example.shopping_mart.repository;

import com.example.shopping_mart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByBrand(String brand);

    Optional<Product> findByName(String productName);

    List<Product> findByBrandAndName(String brand, String productName);

    Long countByBrandAndName(String brand, String productName);

    boolean existsByName(String name);

    boolean existsByCategoryName(String category);

    boolean existsByBrand(String brand);
}
