package com.example.shopping_mart.service.cart;

import com.example.shopping_mart.exceptions.ResourceNotFoundException;
import com.example.shopping_mart.model.Cart;
import com.example.shopping_mart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);


    @Override
    public Optional<Cart> getCart(Long id) {
        return Optional.ofNullable(cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found")));
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        // Clear all items (orphanRemoval=true will delete them from DB)
        cart.getItems().clear();

        // Reset total amount
        cart.setTotalAmount(BigDecimal.ZERO);

        // No need for explicit save with @Transactional
    }

    @Transactional
    @Override
    public BigDecimal getTotalPrice(Long id) {
        return cartRepository.findById(id)
                .map(Cart::getTotalAmount)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));
    }

    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return null;
    }
}
