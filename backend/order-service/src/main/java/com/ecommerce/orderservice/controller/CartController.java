package com.ecommerce.orderservice.controller;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.orderservice.dto.CartItemRequest;
import com.ecommerce.orderservice.dto.CartItemResponse;
import com.ecommerce.orderservice.dto.CartResponse;
import com.ecommerce.orderservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart", description = "Shopping cart management API")
public class CartController {

    private final CartService cartService;

    // Temporary: In production, get userId from JWT token
    private static final Long TEMP_USER_ID = 1L;

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Add a product to the shopping cart")
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(
            @Valid @RequestBody CartItemRequest request) {
        log.info("Adding item to cart: {}", request);

        CartItemResponse response = cartService.addToCart(TEMP_USER_ID, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart", response));
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Update cart item", description = "Update quantity of a cart item")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        log.info("Updating cart item {}: quantity={}", id, quantity);

        CartItemResponse response = cartService.updateCartItem(TEMP_USER_ID, id, quantity);

        return ResponseEntity.ok(ApiResponse.success("Cart item updated", response));
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Remove item from cart", description = "Remove a product from the shopping cart")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Long id) {
        log.info("Removing item from cart: {}", id);

        cartService.removeFromCart(TEMP_USER_ID, id);

        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", null));
    }

    @GetMapping
    @Operation(summary = "Get cart", description = "Get current user's shopping cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        log.info("Fetching cart for user");

        CartResponse response = cartService.getCart(TEMP_USER_ID);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Remove all items from the shopping cart")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        log.info("Clearing cart for user");

        cartService.clearCart(TEMP_USER_ID);

        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }
}
