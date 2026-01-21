package com.ecommerce.orderservice.controller;

import com.ecommerce.common.constants.AppConstants;
import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.dto.PagedResponse;
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "Order management API")
public class OrderController {

    private final OrderService orderService;

    // Temporary: In production, get userId from JWT token
    private static final Long TEMP_USER_ID = 1L;

    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order from cart items")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request) {
        log.info("Creating order for user");

        OrderResponse response = orderService.createOrder(TEMP_USER_ID, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order", description = "Get order details by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id) {
        log.info("Fetching order: {}", id);

        OrderResponse response = orderService.getOrder(TEMP_USER_ID, id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get user orders", description = "Get all orders for the current user")
    public ResponseEntity<ApiResponse<PagedResponse<OrderResponse>>> getUserOrders(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("Fetching orders for user");

        Sort.Direction direction = sortDir.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PagedResponse<OrderResponse> response = orderService.getUserOrders(TEMP_USER_ID, pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an existing order")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long id) {
        log.info("Cancelling order: {}", id);

        OrderResponse response = orderService.cancelOrder(TEMP_USER_ID, id);

        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", response));
    }
}
