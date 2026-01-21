package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.CartItemRequest;
import com.ecommerce.orderservice.dto.CartItemResponse;
import com.ecommerce.orderservice.dto.CartResponse;

public interface CartService {

    CartItemResponse addToCart(Long userId, CartItemRequest request);

    CartItemResponse updateCartItem(Long userId, Long cartItemId, Integer quantity);

    void removeFromCart(Long userId, Long cartItemId);

    CartResponse getCart(Long userId);

    void clearCart(Long userId);
}
