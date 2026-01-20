package com.ecommerce.userservice.service.impl;

import com.ecommerce.common.constants.AppConstants;
import com.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.ecommerce.common.exception.ValidationException;
import com.ecommerce.userservice.dto.JwtResponse;
import com.ecommerce.userservice.dto.UserLoginRequest;
import com.ecommerce.userservice.dto.UserRegistrationRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.entity.Role;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.repository.RoleRepository;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.security.JwtTokenProvider;
import com.ecommerce.userservice.security.UserPrincipal;
import com.ecommerce.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authentication Service Implementation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", request.getEmail());
        }

        // Get or create CUSTOMER role
        Role customerRole = roleRepository.findByName(AppConstants.ROLE_CUSTOMER)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(AppConstants.ROLE_CUSTOMER)
                            .description("Customer role")
                            .build();
                    return roleRepository.save(newRole);
                });

        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .status(AppConstants.USER_STATUS_ACTIVE)
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .roles(new HashSet<>(Set.of(customerRole)))
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(UserLoginRequest request) {
        log.info("User login attempt for email: {}", request.getEmail());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = tokenProvider.generateAccessToken(userPrincipal);
        String refreshToken = tokenProvider.generateRefreshToken(userPrincipal);

        log.info("User logged in successfully: {}", request.getEmail());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(userPrincipal.getId())
                .email(userPrincipal.getEmail())
                .firstName(getUserFirstName(userPrincipal.getId()))
                .lastName(getUserLastName(userPrincipal.getId()))
                .roles(userPrincipal.getAuthorities().stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toSet()))
                .expiresIn(tokenProvider.getExpirationMs())
                .build();
    }

    @Override
    @Transactional
    public void logout(Long userId) {
        log.info("User logout for ID: {}", userId);
        // In a minimal implementation, we just log the logout
        // In a full implementation, we would revoke refresh tokens
    }

    /**
     * Map User entity to UserResponse DTO
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .isEmailVerified(user.getIsEmailVerified())
                .isPhoneVerified(user.getIsPhoneVerified())
                .oauthProvider(user.getOauthProvider())
                .profileImageUrl(user.getProfileImageUrl())
                .status(user.getStatus())
                .preferredCurrencyId(user.getPreferredCurrencyId())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Get user first name by ID
     */
    private String getUserFirstName(Long userId) {
        return userRepository.findById(userId)
                .map(User::getFirstName)
                .orElse("");
    }

    /**
     * Get user last name by ID
     */
    private String getUserLastName(Long userId) {
        return userRepository.findById(userId)
                .map(User::getLastName)
                .orElse("");
    }
}
