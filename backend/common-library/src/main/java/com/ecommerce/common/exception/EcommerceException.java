package com.ecommerce.common.exception;

/**
 * Base exception for all custom exceptions in the e-commerce platform
 */
public class EcommerceException extends RuntimeException {

    private final String errorCode;

    public EcommerceException(String message) {
        super(message);
        this.errorCode = "ECOMMERCE_ERROR";
    }

    public EcommerceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public EcommerceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ECOMMERCE_ERROR";
    }

    public EcommerceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
