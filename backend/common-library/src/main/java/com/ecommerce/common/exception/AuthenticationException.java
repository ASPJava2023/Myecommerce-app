package com.ecommerce.common.exception;

/**
 * Exception thrown when authentication fails
 * HTTP Status: 401 UNAUTHORIZED
 */
public class AuthenticationException extends EcommerceException {

    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_FAILED");
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, "AUTHENTICATION_FAILED", cause);
    }
}
