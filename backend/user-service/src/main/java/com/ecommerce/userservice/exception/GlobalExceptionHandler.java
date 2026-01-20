package com.ecommerce.userservice.exception;

import com.ecommerce.common.constants.ErrorCodes;
import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.dto.ErrorDetails;
import com.ecommerce.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 * Centralized exception handling for all controllers
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle validation exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.error("Validation error: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ErrorCodes.VALIDATION_ERROR)
                .message("Validation failed")
                .fieldErrors(fieldErrors)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errorDetails));
    }

    /**
     * Handle resource not found exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle resource already exists exception
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex,
            WebRequest request) {
        log.error("Resource already exists: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle validation exception
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            ValidationException ex,
            WebRequest request) {
        log.error("Validation error: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle authentication exception
     */
    @ExceptionHandler({ AuthenticationException.class, BadCredentialsException.class })
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
            Exception ex,
            WebRequest request) {
        log.error("Authentication error: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ErrorCodes.AUTHENTICATION_FAILED)
                .message("Invalid email or password")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authentication failed", errorDetails));
    }

    /**
     * Handle unauthorized exception
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(
            UnauthorizedException ex,
            WebRequest request) {
        log.error("Unauthorized access: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle username not found exception
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            WebRequest request) {
        log.error("User not found: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ErrorCodes.USER_NOT_FOUND)
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected error: ", ex);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ErrorCodes.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", errorDetails));
    }
}
