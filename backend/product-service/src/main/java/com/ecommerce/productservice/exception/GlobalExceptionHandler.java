package com.ecommerce.productservice.exception;

import com.ecommerce.common.constants.ErrorCodes;
import com.ecommerce.common.dto.*;
import com.ecommerce.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errorDetails));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error: ", ex);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(ErrorCodes.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", errorDetails));
    }
}
