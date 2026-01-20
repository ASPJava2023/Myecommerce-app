package com.ecommerce.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * User Response DTO
 * Used for returning user information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;
    private String oauthProvider;
    private String profileImageUrl;
    private String status;
    private Long preferredCurrencyId;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
