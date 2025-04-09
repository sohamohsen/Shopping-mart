package com.example.shopping_mart.controller;

import com.example.shopping_mart.exceptions.ResourceNotFoundException;
import com.example.shopping_mart.model.Cart;
import com.example.shopping_mart.response.ApiResponse;
import com.example.shopping_mart.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/my_cart/{id}")
    public ResponseEntity<ApiResponse> getMyCart(@PathVariable Long id) {
        try {
            Optional<Cart> cart = cartService.getCart(id);
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/clear/{cardId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cardId) {
        try {
            cartService.clearCart(cardId);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/totalprice/{id}")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long id) {
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
