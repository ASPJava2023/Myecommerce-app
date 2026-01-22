package com.ecommerce.orderservice.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.dto.CartItemRequest;
import com.ecommerce.orderservice.dto.CartItemResponse;
import com.ecommerce.orderservice.dto.CartResponse;
import com.ecommerce.orderservice.entity.CartItem;
import com.ecommerce.orderservice.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Unit Tests")
class CartServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartServiceImpl cartService;

    private CartItemRequest cartItemRequest;
    private CartItem testCartItem;
    private Map<String, Object> productData;

    @BeforeEach
    void setUp() {
        cartItemRequest = CartItemRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

        testCartItem = CartItem.builder()
                .id(1L)
                .userId(1L)
                .productId(1L)
                .productName("Test Product")
                .price(new BigDecimal("50.00"))
                .quantity(2)
                .productImageUrl("http://example.com/image.jpg")
                .build();

        productData = new HashMap<>();
        productData.put("name", "Test Product");
        productData.put("price", 50.00);
        productData.put("imageUrl", "http://example.com/image.jpg");
    }

    @Test
    @DisplayName("Should successfully add new item to cart")
    void testAddToCart_NewItem_Success() {
        // Given
        when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

        Map<String, Object> response = new HashMap<>();
        response.put("data", productData);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        // When
        CartItemResponse result = cartService.addToCart(1L, cartItemRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getProductName()).isEqualTo("Test Product");

        verify(cartItemRepository).findByUserIdAndProductId(1L, 1L);
        verify(restTemplate).getForObject(anyString(), eq(Map.class));
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should update quantity when adding existing item to cart")
    void testAddToCart_ExistingItem_UpdateQuantity() {
        // Given
        when(cartItemRepository.findByUserIdAndProductId(1L, 1L))
                .thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        // When
        CartItemResponse result = cartService.addToCart(1L, cartItemRequest);

        // Then
        assertThat(result).isNotNull();
        verify(cartItemRepository).findByUserIdAndProductId(1L, 1L);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(restTemplate, never()).getForObject(anyString(), eq(Map.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when product service fails")
    void testAddToCart_ProductServiceFailure() {
        // Given
        when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        // When & Then
        assertThatThrownBy(() -> cartService.addToCart(1L, cartItemRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch product details");

        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when product not found")
    void testAddToCart_ProductNotFound() {
        // Given
        when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

        Map<String, Object> response = new HashMap<>();
        // No 'data' key in response
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        // When & Then
        assertThatThrownBy(() -> cartService.addToCart(1L, cartItemRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch product details");
    }

    @Test
    @DisplayName("Should successfully update cart item quantity")
    void testUpdateCartItem_Success() {
        // Given
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        // When
        CartItemResponse result = cartService.updateCartItem(1L, 1L, 5);

        // Then
        assertThat(result).isNotNull();
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent cart item")
    void testUpdateCartItem_NotFound() {
        // Given
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.updateCartItem(1L, 1L, 5))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cart item not found");

        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when cart item belongs to different user")
    void testUpdateCartItem_UnauthorizedAccess() {
        // Given
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        // When & Then
        assertThatThrownBy(() -> cartService.updateCartItem(2L, 1L, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cart item does not belong to user");

        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when quantity is invalid")
    void testUpdateCartItem_InvalidQuantity() {
        // Given
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        // When & Then
        assertThatThrownBy(() -> cartService.updateCartItem(1L, 1L, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than 0");

        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should successfully remove item from cart")
    void testRemoveFromCart_Success() {
        // Given
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        // When
        cartService.removeFromCart(1L, 1L);

        // Then
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).delete(testCartItem);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when removing non-existent cart item")
    void testRemoveFromCart_NotFound() {
        // Given
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.removeFromCart(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when removing cart item of different user")
    void testRemoveFromCart_UnauthorizedAccess() {
        // Given
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        // When & Then
        assertThatThrownBy(() -> cartService.removeFromCart(2L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cart item does not belong to user");

        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    @DisplayName("Should successfully get cart with total calculation")
    void testGetCart_Success() {
        // Given
        CartItem item2 = CartItem.builder()
                .id(2L)
                .userId(1L)
                .productId(2L)
                .productName("Product 2")
                .price(new BigDecimal("30.00"))
                .quantity(1)
                .build();

        when(cartItemRepository.findByUserId(1L)).thenReturn(List.of(testCartItem, item2));

        // When
        CartResponse result = cartService.getCart(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotalItems()).isEqualTo(2);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("130.00"));
        verify(cartItemRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("Should return empty cart when no items exist")
    void testGetCart_Empty() {
        // Given
        when(cartItemRepository.findByUserId(1L)).thenReturn(List.of());

        // When
        CartResponse result = cartService.getCart(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalItems()).isEqualTo(0);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should successfully clear cart")
    void testClearCart_Success() {
        // When
        cartService.clearCart(1L);

        // Then
        verify(cartItemRepository).deleteByUserId(1L);
    }
}
