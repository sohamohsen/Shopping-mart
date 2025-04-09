package com.example.shopping_mart.service.cart;

import com.example.shopping_mart.model.CartItem;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

public interface ICartItemService {
    void addItemToCart(long cartId, long productId, int quantity);
    void removeItemFromCart(long cartId, long productId);
    public void updateItemQuantity(long cartId, long productId, int quantity);

    CartItem getCartItem(long cartId, long productId);
}
