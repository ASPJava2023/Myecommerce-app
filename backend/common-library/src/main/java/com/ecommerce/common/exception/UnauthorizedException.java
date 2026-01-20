package com.ecommerce.common.exception;

/**
 * Exception thrown when user is not authorized to perform an action
 * HTTP Status: 403 FORBIDDEN
 */
public class UnauthorizedException extends EcommerceException {

    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }

    public UnauthorizedException() {
        super("You are not authorized to perform this action", "UNAUTHORIZED");
    }
}
