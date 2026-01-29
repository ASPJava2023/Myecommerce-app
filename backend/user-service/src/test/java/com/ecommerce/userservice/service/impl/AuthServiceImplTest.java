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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRegistrationRequest registrationRequest;
    private User testUser;
    private Role customerRole;

    @BeforeEach
    void setUp() {
        // Setup test data
        registrationRequest = UserRegistrationRequest.builder()
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();

        customerRole = Role.builder()
                .id(1L)
                .name(AppConstants.ROLE_CUSTOMER)
                .description("Customer role")
                .build();

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .status(AppConstants.USER_STATUS_ACTIVE)
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .roles(new HashSet<>(Set.of(customerRole)))
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void testRegister_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(AppConstants.ROLE_CUSTOMER)).thenReturn(Optional.of(customerRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse response = authService.register(registrationRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getRoles()).contains(AppConstants.ROLE_CUSTOMER);

        verify(userRepository).existsByEmail("test@example.com");
        verify(roleRepository).findByName(AppConstants.ROLE_CUSTOMER);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when passwords do not match")
    void testRegister_PasswordMismatch() {
        // Given
        registrationRequest.setConfirmPassword("differentPassword");

        // When & Then
        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Passwords do not match");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceAlreadyExistsException when email already exists")
    void testRegister_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("test@example.com");

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should create customer role if it doesn't exist during registration")
    void testRegister_CreateRoleIfNotExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(AppConstants.ROLE_CUSTOMER)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(customerRole);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse response = authService.register(registrationRequest);

        // Then
        assertThat(response).isNotNull();
        verify(roleRepository).save(any(Role.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void testLogin_Success() {
        // Given
        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = new UserPrincipal(
                1L,
                "test@example.com",
                "encodedPassword",
                Set.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")),
                true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(tokenProvider.generateAccessToken(any(UserPrincipal.class))).thenReturn("accessToken");
        when(tokenProvider.generateRefreshToken(any(UserPrincipal.class))).thenReturn("refreshToken");
        when(tokenProvider.getExpirationMs()).thenReturn(3600000L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        JwtResponse response = authService.login(loginRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@example.com");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateAccessToken(userPrincipal);
        verify(tokenProvider).generateRefreshToken(userPrincipal);
    }

    @Test
    @DisplayName("Should successfully logout user")
    void testLogout_Success() {
        // Given
        Long userId = 1L;

        // When & Then
        // Should not throw any exception
        authService.logout(userId);

        // Verify the method completes without errors
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Should map User entity to UserResponse correctly")
    void testMapToUserResponse() {
        // This is tested implicitly through register() tests
        // The mapping logic is verified when we check the response fields
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(AppConstants.ROLE_CUSTOMER)).thenReturn(Optional.of(customerRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = authService.register(registrationRequest);

        assertThat(response.getId()).isEqualTo(testUser.getId());
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(response.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(response.getPhoneNumber()).isEqualTo(testUser.getPhoneNumber());
        assertThat(response.getIsEmailVerified()).isEqualTo(testUser.getIsEmailVerified());
        assertThat(response.getIsPhoneVerified()).isEqualTo(testUser.getIsPhoneVerified());
    }
}
