package com.ecommerce.orderservice.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.dto.CartItemRequest;
import com.ecommerce.orderservice.dto.CartItemResponse;
import com.ecommerce.orderservice.dto.CartResponse;
import com.ecommerce.orderservice.entity.CartItem;
import com.ecommerce.orderservice.repository.CartItemRepository;
import com.ecommerce.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8083/api/products";

    @Override
    @Transactional
    public CartItemResponse addToCart(Long userId, CartItemRequest request) {
        log.info("Adding product {} to cart for user {}", request.getProductId(), userId);

        // Check if product already exists in cart
        var existingItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            CartItem updated = cartItemRepository.save(cartItem);
            return mapToResponse(updated);
        }

        // Fetch product details from Product Service
        Map<String, Object> productData = fetchProductDetails(request.getProductId());

        // Create new cart item
        CartItem cartItem = CartItem.builder()
                .userId(userId)
                .productId(request.getProductId())
                .productName((String) productData.get("name"))
                .price(new BigDecimal(productData.get("price").toString()))
                .quantity(request.getQuantity())
                .productImageUrl((String) productData.get("imageUrl"))
                .build();

        CartItem saved = cartItemRepository.save(cartItem);
        log.info("Product added to cart successfully: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        log.info("Updating cart item {} for user {}", cartItemId, userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cart item does not belong to user");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        cartItem.setQuantity(quantity);
        CartItem updated = cartItemRepository.save(cartItem);

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long cartItemId) {
        log.info("Removing cart item {} for user {}", cartItemId, userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cart item does not belong to user");
        }

        cartItemRepository.delete(cartItem);
        log.info("Cart item removed successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        log.info("Fetching cart for user {}", userId);

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<CartItemResponse> items = cartItems.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .items(items)
                .totalAmount(totalAmount)
                .totalItems(cartItems.size())
                .build();
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        log.info("Clearing cart for user {}", userId);
        cartItemRepository.deleteByUserId(userId);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fetchProductDetails(Long productId) {
        try {
            String url = PRODUCT_SERVICE_URL + "/" + productId;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("data")) {
                return (Map<String, Object>) response.get("data");
            }

            throw new ResourceNotFoundException("Product not found: " + productId);
        } catch (Exception e) {
            log.error("Error fetching product details: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch product details", e);
        }
    }

    private CartItemResponse mapToResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(cartItem.getSubtotal())
                .productImageUrl(cartItem.getProductImageUrl())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }
}
