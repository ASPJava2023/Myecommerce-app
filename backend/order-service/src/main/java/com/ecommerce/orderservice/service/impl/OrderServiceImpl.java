package com.ecommerce.orderservice.service.impl;

import com.ecommerce.common.dto.PagedResponse;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.dto.OrderItemResponse;
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.entity.CartItem;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.repository.CartItemRepository;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        log.info("Creating order for user {}", userId);

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Calculate total
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = Order.builder()
                .userId(userId)
                .orderNumber(generateOrderNumber())
                .status(Order.OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .shippingAddress(request.getShippingAddress().toString())
                .billingAddress(request.getBillingAddress() != null ? request.getBillingAddress().toString()
                        : request.getShippingAddress().toString())
                .paymentMethod(request.getPaymentMethod())
                .notes(request.getNotes())
                .orderDate(LocalDateTime.now())
                .build();

        // Convert cart items to order items
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .productId(cartItem.getProductId())
                        .productName(cartItem.getProductName())
                        .price(cartItem.getPrice())
                        .quantity(cartItem.getQuantity())
                        .subtotal(cartItem.getSubtotal())
                        .productImageUrl(cartItem.getProductImageUrl())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteByUserId(userId);

        log.info("Order created successfully: {}", savedOrder.getOrderNumber());

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long userId, Long orderId) {
        log.info("Fetching order {} for user {}", orderId, userId);

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        log.info("Fetching orders for user {}", userId);

        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);

        List<OrderResponse> orders = orderPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<OrderResponse>builder()
                .content(orders)
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .last(orderPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        log.info("Cancelling order {} for user {}", orderId, userId);

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel delivered order");
        }

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order already cancelled");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        Order updated = orderRepository.save(order);

        log.info("Order cancelled successfully: {}", order.getOrderNumber());

        return mapToResponse(updated);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf((int) (Math.random() * 10000));
        return "ORD-" + timestamp + "-" + random;
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapOrderItemToResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .paymentMethod(order.getPaymentMethod())
                .orderDate(order.getOrderDate())
                .deliveryDate(order.getDeliveryDate())
                .notes(order.getNotes())
                .items(items)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderItemResponse mapOrderItemToResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productSku(item.getProductSku())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .productImageUrl(item.getProductImageUrl())
                .build();
    }
}
