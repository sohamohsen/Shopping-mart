package com.example.shopping_mart.repository;

import com.example.shopping_mart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
