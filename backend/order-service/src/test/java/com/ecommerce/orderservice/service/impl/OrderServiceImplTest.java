package com.ecommerce.orderservice.service.impl;

import com.ecommerce.common.dto.PagedResponse;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.dto.AddressDTO;
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.entity.CartItem;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.repository.CartItemRepository;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Unit Tests")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest orderRequest;
    private Order testOrder;
    private List<CartItem> cartItems;
    private AddressDTO shippingAddress;

    @BeforeEach
    void setUp() {
        shippingAddress = AddressDTO.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        orderRequest = OrderRequest.builder()
                .shippingAddress(shippingAddress)
                .billingAddress(shippingAddress)
                .paymentMethod("CREDIT_CARD")
                .notes("Test order")
                .build();

        CartItem cartItem1 = CartItem.builder()
                .id(1L)
                .userId(1L)
                .productId(1L)
                .productName("Product 1")
                .price(new BigDecimal("50.00"))
                .quantity(2)
                .build();

        CartItem cartItem2 = CartItem.builder()
                .id(2L)
                .userId(1L)
                .productId(2L)
                .productName("Product 2")
                .price(new BigDecimal("30.00"))
                .quantity(1)
                .build();

        cartItems = List.of(cartItem1, cartItem2);

        OrderItem orderItem1 = OrderItem.builder()
                .id(1L)
                .productId(1L)
                .productName("Product 1")
                .price(new BigDecimal("50.00"))
                .quantity(2)
                .subtotal(new BigDecimal("100.00"))
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .id(2L)
                .productId(2L)
                .productName("Product 2")
                .price(new BigDecimal("30.00"))
                .quantity(1)
                .subtotal(new BigDecimal("30.00"))
                .build();

        testOrder = Order.builder()
                .id(1L)
                .userId(1L)
                .orderNumber("ORD-20260122-1234")
                .status(Order.OrderStatus.PENDING)
                .totalAmount(new BigDecimal("130.00"))
                .shippingAddress(shippingAddress.toString())
                .billingAddress(shippingAddress.toString())
                .paymentMethod("CREDIT_CARD")
                .orderDate(LocalDateTime.now())
                .items(new ArrayList<>(List.of(orderItem1, orderItem2)))
                .build();

        orderItem1.setOrder(testOrder);
        orderItem2.setOrder(testOrder);
    }

    @Test
    @DisplayName("Should successfully create an order")
    void testCreateOrder_Success() {
        // Given
        when(cartItemRepository.findByUserId(1L)).thenReturn(cartItems);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponse response = orderService.createOrder(1L, orderRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getOrderNumber()).isNotNull();
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getTotalAmount()).isEqualByComparingTo(new BigDecimal("130.00"));
        assertThat(response.getItems()).hasSize(2);

        verify(cartItemRepository).findByUserId(1L);
        verify(orderRepository).save(any(Order.class));
        verify(cartItemRepository).deleteByUserId(1L);
    }

    @Test
    @DisplayName("Should throw IllegalStateException when cart is empty")
    void testCreateOrder_EmptyCart() {
        // Given
        when(cartItemRepository.findByUserId(1L)).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> orderService.createOrder(1L, orderRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cart is empty");

        verify(cartItemRepository).findByUserId(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should use shipping address as billing address when billing address is null")
    void testCreateOrder_NullBillingAddress() {
        // Given
        orderRequest.setBillingAddress(null);
        when(cartItemRepository.findByUserId(1L)).thenReturn(cartItems);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponse response = orderService.createOrder(1L, orderRequest);

        // Then
        assertThat(response).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should successfully get order by ID and user ID")
    void testGetOrder_Success() {
        // Given
        when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testOrder));

        // When
        OrderResponse response = orderService.getOrder(1L, 1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getOrderNumber()).isEqualTo("ORD-20260122-1234");
        verify(orderRepository).findByIdAndUserId(1L, 1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order not found")
    void testGetOrder_NotFound() {
        // Given
        when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.getOrder(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    @DisplayName("Should successfully get user orders with pagination")
    void testGetUserOrders_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(testOrder), pageable, 1);
        when(orderRepository.findByUserId(1L, pageable)).thenReturn(orderPage);

        // When
        PagedResponse<OrderResponse> response = orderService.getUserOrders(1L, pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getPageNumber()).isEqualTo(0);
        verify(orderRepository).findByUserId(1L, pageable);
    }

    @Test
    @DisplayName("Should successfully cancel a pending order")
    void testCancelOrder_Success() {
        // Given
        when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponse response = orderService.cancelOrder(1L, 1L);

        // Then
        assertThat(response).isNotNull();
        verify(orderRepository).findByIdAndUserId(1L, 1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when cancelling delivered order")
    void testCancelOrder_AlreadyDelivered() {
        // Given
        testOrder.setStatus(Order.OrderStatus.DELIVERED);
        when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testOrder));

        // When & Then
        assertThatThrownBy(() -> orderService.cancelOrder(1L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot cancel delivered order");

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when cancelling already cancelled order")
    void testCancelOrder_AlreadyCancelled() {
        // Given
        testOrder.setStatus(Order.OrderStatus.CANCELLED);
        when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testOrder));

        // When & Then
        assertThatThrownBy(() -> orderService.cancelOrder(1L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order already cancelled");

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when cancelling non-existent order")
    void testCancelOrder_NotFound() {
        // Given
        when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.cancelOrder(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
