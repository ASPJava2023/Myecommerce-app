package com.ecommerce.common.constants;

/**
 * Application-wide constants
 */
public final class AppConstants {

    private AppConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Pagination
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String MAX_PAGE_SIZE = "100";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    // JWT
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_USER_ID = "userId";
    public static final String JWT_CLAIM_EMAIL = "email";
    public static final String JWT_CLAIM_ROLES = "roles";

    // Headers
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String USER_ID_HEADER = "X-User-ID";
    public static final String TENANT_ID_HEADER = "X-Tenant-ID";

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_SELLER = "SELLER";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    // OAuth2
    public static final String OAUTH2_PROVIDER_GOOGLE = "GOOGLE";
    public static final String OAUTH2_PROVIDER_FACEBOOK = "FACEBOOK";

    // Order Status
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_PROCESSING = "PROCESSING";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    public static final String ORDER_STATUS_REFUNDED = "REFUNDED";

    // Payment Status
    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_COMPLETED = "COMPLETED";
    public static final String PAYMENT_STATUS_FAILED = "FAILED";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";

    // Product Approval Status
    public static final String APPROVAL_STATUS_PENDING = "PENDING";
    public static final String APPROVAL_STATUS_APPROVED = "APPROVED";
    public static final String APPROVAL_STATUS_REJECTED = "REJECTED";

    // Seller Status
    public static final String SELLER_STATUS_PENDING = "PENDING";
    public static final String SELLER_STATUS_APPROVED = "APPROVED";
    public static final String SELLER_STATUS_REJECTED = "REJECTED";
    public static final String SELLER_STATUS_SUSPENDED = "SUSPENDED";

    // User Status
    public static final String USER_STATUS_ACTIVE = "ACTIVE";
    public static final String USER_STATUS_INACTIVE = "INACTIVE";
    public static final String USER_STATUS_SUSPENDED = "SUSPENDED";

    // Notification Types
    public static final String NOTIFICATION_TYPE_EMAIL = "EMAIL";
    public static final String NOTIFICATION_TYPE_SMS = "SMS";
    public static final String NOTIFICATION_TYPE_WHATSAPP = "WHATSAPP";

    // Email Templates
    public static final String TEMPLATE_WELCOME = "WELCOME";
    public static final String TEMPLATE_ORDER_CONFIRMATION = "ORDER_CONFIRMATION";
    public static final String TEMPLATE_PASSWORD_RESET = "PASSWORD_RESET";
    public static final String TEMPLATE_SELLER_APPROVAL = "SELLER_APPROVAL";
    public static final String TEMPLATE_PRODUCT_APPROVAL = "PRODUCT_APPROVAL";

    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";

    // Currency
    public static final String DEFAULT_CURRENCY = "USD";

    // File Upload
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final String[] ALLOWED_IMAGE_TYPES = { "image/jpeg", "image/png", "image/jpg", "image/webp" };

    // Validation
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
}
