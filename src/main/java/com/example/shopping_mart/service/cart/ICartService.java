package com.example.shopping_mart.service.cart;

import com.example.shopping_mart.model.Cart;

import java.math.BigDecimal;
import java.util.Optional;

public interface ICartService {
    Optional<Cart> getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeNewCart();

    Cart getCartByUserId(Long userId);
}
