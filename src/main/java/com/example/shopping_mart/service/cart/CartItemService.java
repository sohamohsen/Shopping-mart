package com.example.shopping_mart.service.cart;

import com.example.shopping_mart.exceptions.ProductNotFoundException;
import com.example.shopping_mart.exceptions.ResourceNotFoundException;
import com.example.shopping_mart.model.Cart;
import com.example.shopping_mart.model.CartItem;
import com.example.shopping_mart.model.Product;
import com.example.shopping_mart.repository.CartItemRepository;
import com.example.shopping_mart.repository.CartRepository;
import com.example.shopping_mart.repository.ProductRepository;
import com.example.shopping_mart.service.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private ProductRepository productRepository;
    private IProductService productService;

    @Override
    public void addItemToCart(long cartId, long productId, int quantity) {
        //1. Get the cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        //2. Get the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        //3. Check if the product already in the cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();
        //4. If Yes, then increase the quantity with the requested quantity
        if(existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        }else {
            //5. If No, then initiate a new CartItem entry.
            CartItem Item = new CartItem();
            Item.setProduct(product);
            Item.setQuantity(quantity);
            Item.setCart(cart);
            cart.getItems().add(Item);
        }
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(long cartId, long productId) {
        // 1. Get the cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        // 2. Find the cart item to remove
        CartItem cartItemToRemove = getCartItem(cartId, productId);

        // 3. Remove the item from cart
        cart.removeItem(cartItemToRemove);

        // 4. Save the updated cart
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void updateItemQuantity(long cartId, long productId, int quantity) {
        // Step 1: Get the cart
        Cart cart = getCartOrThrow(cartId);

        // Step 2: Find and update the cart item
        CartItem cartItem = findCartItemOrThrow(cart, productId);
        updateCartItem(cartItem, quantity);

        // Step 3: Update cart totals and save
        updateCartTotals(cart);
    }

    // Extracted helper methods
    private Cart getCartOrThrow(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));
    }

    private CartItem findCartItemOrThrow(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId()== productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found in cart with id: " + productId));
    }

    private void updateCartItem(CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(cartItem.getProduct().getPrice());
        cartItem.setTotalPrice(); // Assuming this calculates quantity × unitPrice
    }

    private void updateCartTotals(Cart cart) {
        BigDecimal totalAmount = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(long cartId, long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
