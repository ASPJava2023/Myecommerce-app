package com.ecommerce.common.exception;

/**
 * Exception thrown when business validation fails
 * HTTP Status: 400 BAD REQUEST
 */
public class ValidationException extends EcommerceException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }

    public ValidationException(String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", cause);
    }
}
