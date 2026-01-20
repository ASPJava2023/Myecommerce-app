package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.JwtResponse;
import com.ecommerce.userservice.dto.UserLoginRequest;
import com.ecommerce.userservice.dto.UserRegistrationRequest;
import com.ecommerce.userservice.dto.UserResponse;

/**
 * Authentication Service Interface
 */
public interface AuthService {

    /**
     * Register a new user
     */
    UserResponse register(UserRegistrationRequest request);

    /**
     * Authenticate user and generate JWT token
     */
    JwtResponse login(UserLoginRequest request);

    /**
     * Logout user (revoke tokens)
     */
    void logout(Long userId);
}
