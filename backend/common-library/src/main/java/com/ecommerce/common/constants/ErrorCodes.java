package com.ecommerce.common.constants;

/**
 * Application-wide error codes
 * Used for programmatic error handling
 */
public final class ErrorCodes {

    private ErrorCodes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // General Errors
    public static final String INTERNAL_SERVER_ERROR = "ERR_INTERNAL_SERVER";
    public static final String VALIDATION_ERROR = "ERR_VALIDATION";
    public static final String RESOURCE_NOT_FOUND = "ERR_RESOURCE_NOT_FOUND";
    public static final String RESOURCE_ALREADY_EXISTS = "ERR_RESOURCE_EXISTS";
    public static final String UNAUTHORIZED = "ERR_UNAUTHORIZED";
    public static final String AUTHENTICATION_FAILED = "ERR_AUTH_FAILED";

    // User Service Errors
    public static final String USER_NOT_FOUND = "ERR_USER_NOT_FOUND";
    public static final String USER_ALREADY_EXISTS = "ERR_USER_EXISTS";
    public static final String INVALID_CREDENTIALS = "ERR_INVALID_CREDENTIALS";
    public static final String EMAIL_ALREADY_EXISTS = "ERR_EMAIL_EXISTS";
    public static final String INVALID_TOKEN = "ERR_INVALID_TOKEN";
    public static final String TOKEN_EXPIRED = "ERR_TOKEN_EXPIRED";
    public static final String REFRESH_TOKEN_EXPIRED = "ERR_REFRESH_TOKEN_EXPIRED";

    // Product Service Errors
    public static final String PRODUCT_NOT_FOUND = "ERR_PRODUCT_NOT_FOUND";
    public static final String CATEGORY_NOT_FOUND = "ERR_CATEGORY_NOT_FOUND";
    public static final String PRODUCT_OUT_OF_STOCK = "ERR_OUT_OF_STOCK";
    public static final String INVALID_PRICE = "ERR_INVALID_PRICE";

    // Order Service Errors
    public static final String ORDER_NOT_FOUND = "ERR_ORDER_NOT_FOUND";
    public static final String PAYMENT_FAILED = "ERR_PAYMENT_FAILED";
    public static final String INVALID_ORDER_STATUS = "ERR_INVALID_ORDER_STATUS";
    public static final String CART_EMPTY = "ERR_CART_EMPTY";
    public static final String INSUFFICIENT_STOCK = "ERR_INSUFFICIENT_STOCK";

    // Seller Service Errors
    public static final String SELLER_NOT_FOUND = "ERR_SELLER_NOT_FOUND";
    public static final String SELLER_NOT_APPROVED = "ERR_SELLER_NOT_APPROVED";
    public static final String SELLER_SUSPENDED = "ERR_SELLER_SUSPENDED";

    // Admin Service Errors
    public static final String ADMIN_ACTION_FAILED = "ERR_ADMIN_ACTION_FAILED";
    public static final String INVALID_APPROVAL_STATUS = "ERR_INVALID_APPROVAL_STATUS";

    // Notification Service Errors
    public static final String EMAIL_SEND_FAILED = "ERR_EMAIL_SEND_FAILED";
    public static final String SMS_SEND_FAILED = "ERR_SMS_SEND_FAILED";
    public static final String TEMPLATE_NOT_FOUND = "ERR_TEMPLATE_NOT_FOUND";
}
