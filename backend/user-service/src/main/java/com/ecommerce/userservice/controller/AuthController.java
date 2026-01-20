package com.ecommerce.userservice.controller;

import com.ecommerce.common.constants.Messages;
import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.userservice.dto.JwtResponse;
import com.ecommerce.userservice.dto.UserLoginRequest;
import com.ecommerce.userservice.dto.UserRegistrationRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.security.UserPrincipal;
import com.ecommerce.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles user registration, login, and logout
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("Registration request received for email: {}", request.getEmail());

        UserResponse userResponse = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(Messages.USER_REGISTERED, userResponse));
    }

    /**
     * Login user
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody UserLoginRequest request) {
        log.info("Login request received for email: {}", request.getEmail());

        JwtResponse jwtResponse = authService.login(request);

        return ResponseEntity.ok(ApiResponse.success(Messages.LOGIN_SUCCESS, jwtResponse));
    }

    /**
     * Logout user
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and revoke tokens")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("Logout request received for user ID: {}", userPrincipal.getId());

        authService.logout(userPrincipal.getId());

        return ResponseEntity.ok(ApiResponse.success(Messages.LOGOUT_SUCCESS, null));
    }

    /**
     * Get current user info
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get authenticated user information")
    public ResponseEntity<ApiResponse<UserPrincipal>> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("Get current user request for ID: {}", userPrincipal.getId());

        return ResponseEntity.ok(ApiResponse.success(userPrincipal));
    }
}
