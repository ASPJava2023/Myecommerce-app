package com.ecommerce.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressDTO shippingAddress;

    @Valid
    private AddressDTO billingAddress;

    private String paymentMethod;
    private String notes;
}
