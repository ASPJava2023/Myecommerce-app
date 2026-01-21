package com.ecommerce.orderservice.service;

import com.ecommerce.common.dto.PagedResponse;
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse createOrder(Long userId, OrderRequest request);

    OrderResponse getOrder(Long userId, Long orderId);

    PagedResponse<OrderResponse> getUserOrders(Long userId, Pageable pageable);

    OrderResponse cancelOrder(Long userId, Long orderId);
}
