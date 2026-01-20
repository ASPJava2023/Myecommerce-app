package com.ecommerce.common.constants;

/**
 * Application-wide message constants
 * Used for consistent user-facing messages
 */
public final class Messages {

    private Messages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Success Messages
    public static final String SUCCESS = "Operation completed successfully";
    public static final String CREATED = "Resource created successfully";
    public static final String UPDATED = "Resource updated successfully";
    public static final String DELETED = "Resource deleted successfully";

    // User Messages
    public static final String USER_REGISTERED = "User registered successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Password reset email sent successfully";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successful";
    public static final String PROFILE_UPDATED = "Profile updated successfully";

    // Product Messages
    public static final String PRODUCT_CREATED = "Product created successfully";
    public static final String PRODUCT_UPDATED = "Product updated successfully";
    public static final String PRODUCT_DELETED = "Product deleted successfully";
    public static final String PRODUCT_APPROVED = "Product approved successfully";
    public static final String PRODUCT_REJECTED = "Product rejected";

    // Order Messages
    public static final String ORDER_PLACED = "Order placed successfully";
    public static final String ORDER_CANCELLED = "Order cancelled successfully";
    public static final String PAYMENT_SUCCESS = "Payment completed successfully";
    public static final String PAYMENT_PENDING = "Payment is pending";

    // Seller Messages
    public static final String SELLER_REGISTERED = "Seller registration submitted for approval";
    public static final String SELLER_APPROVED = "Seller account approved";
    public static final String SELLER_REJECTED = "Seller account rejected";

    // Cart Messages
    public static final String ITEM_ADDED_TO_CART = "Item added to cart";
    public static final String ITEM_REMOVED_FROM_CART = "Item removed from cart";
    public static final String CART_CLEARED = "Cart cleared";

    // Error Messages
    public static final String INTERNAL_ERROR = "An internal error occurred. Please try again later";
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String UNAUTHORIZED_ACCESS = "You are not authorized to access this resource";
    public static final String AUTHENTICATION_REQUIRED = "Authentication required";
    public static final String INVALID_REQUEST = "Invalid request";
}
