-- ============================================
-- E-COMMERCE PLATFORM DATABASE SCHEMA
-- ============================================
-- Version: 1.0
-- Database: MySQL 8.0+
-- Character Set: utf8mb4
-- Collation: utf8mb4_unicode_ci
-- ============================================
-- ============================================
-- USER SERVICE DATABASE
-- ============================================
CREATE DATABASE IF NOT EXISTS ecommerce_user CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce_user;
-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    is_email_verified BOOLEAN DEFAULT FALSE,
    is_phone_verified BOOLEAN DEFAULT FALSE,
    oauth_provider VARCHAR(50),
    -- GOOGLE, FACEBOOK, null for local
    oauth_provider_id VARCHAR(255),
    profile_image_url VARCHAR(500),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    -- ACTIVE, INACTIVE, SUSPENDED
    preferred_currency_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version INT DEFAULT 0,
    -- Optimistic locking
    INDEX idx_email (email),
    INDEX idx_oauth (oauth_provider, oauth_provider_id),
    INDEX idx_status (status)
) ENGINE = InnoDB;
-- Roles Table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    -- ADMIN, SELLER, CUSTOMER
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;
-- User Roles Mapping
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(100),
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE = InnoDB;
-- Addresses Table
CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    address_type VARCHAR(20) DEFAULT 'SHIPPING',
    -- SHIPPING, BILLING, BOTH
    full_name VARCHAR(200) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_default (user_id, is_default)
) ENGINE = InnoDB;
-- Password Reset Tokens
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_user_id (user_id)
) ENGINE = InnoDB;
-- Refresh Tokens
CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(500) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    is_revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_user_id (user_id)
) ENGINE = InnoDB;
-- ============================================
-- SELLER SERVICE DATABASE
-- ============================================
CREATE DATABASE IF NOT EXISTS ecommerce_seller CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce_seller;
-- Sellers Table
CREATE TABLE sellers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    business_name VARCHAR(255) NOT NULL,
    business_email VARCHAR(255) NOT NULL,
    business_phone VARCHAR(20) NOT NULL,
    business_registration_number VARCHAR(100),
    tax_id VARCHAR(100),
    business_type VARCHAR(50),
    -- INDIVIDUAL, COMPANY, PARTNERSHIP
    business_address_line1 VARCHAR(255) NOT NULL,
    business_address_line2 VARCHAR(255),
    business_city VARCHAR(100) NOT NULL,
    business_state VARCHAR(100) NOT NULL,
    business_country VARCHAR(100) NOT NULL,
    business_postal_code VARCHAR(20) NOT NULL,
    bank_account_name VARCHAR(255),
    bank_account_number VARCHAR(100),
    bank_name VARCHAR(255),
    bank_ifsc_code VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDING',
    -- PENDING, APPROVED, REJECTED, SUSPENDED
    approval_date TIMESTAMP NULL,
    approved_by BIGINT,
    rejection_reason TEXT,
    logo_url VARCHAR(500),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version INT DEFAULT 0,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_business_name (business_name)
) ENGINE = InnoDB;
-- Seller Products (Ownership mapping)
CREATE TABLE seller_products (
    seller_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    stock_quantity INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (seller_id, product_id),
    FOREIGN KEY (seller_id) REFERENCES sellers(id) ON DELETE CASCADE,
    INDEX idx_seller_id (seller_id),
    INDEX idx_product_id (product_id)
) ENGINE = InnoDB;
-- ============================================
-- PRODUCT SERVICE DATABASE
-- ============================================
CREATE DATABASE IF NOT EXISTS ecommerce_product CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce_product;
-- Categories Table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    parent_category_id BIGINT,
    image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    FOREIGN KEY (parent_category_id) REFERENCES categories(id) ON DELETE
    SET NULL,
        INDEX idx_parent (parent_category_id),
        INDEX idx_active (is_active)
) ENGINE = InnoDB;
-- Products Table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sku VARCHAR(100) UNIQUE,
    brand VARCHAR(100),
    weight DECIMAL(10, 2),
    -- in kg
    dimensions VARCHAR(100),
    -- LxWxH in cm
    is_active BOOLEAN DEFAULT TRUE,
    is_featured BOOLEAN DEFAULT FALSE,
    approval_status VARCHAR(20) DEFAULT 'PENDING',
    -- PENDING, APPROVED, REJECTED
    approved_by BIGINT,
    approval_date TIMESTAMP NULL,
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version INT DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_seller_id (seller_id),
    INDEX idx_category_id (category_id),
    INDEX idx_sku (sku),
    INDEX idx_approval_status (approval_status),
    INDEX idx_active (is_active),
    FULLTEXT idx_search (name, description)
) ENGINE = InnoDB;
-- Product Images
CREATE TABLE product_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
) ENGINE = InnoDB;
-- Currencies Table
CREATE TABLE currencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    -- USD, EUR, INR
    name VARCHAR(100) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    exchange_rate DECIMAL(15, 6) DEFAULT 1.0,
    -- Relative to base currency
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code)
) ENGINE = InnoDB;
-- Product Prices (Multi-currency support)
CREATE TABLE product_prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    base_price DECIMAL(15, 2) NOT NULL,
    sale_price DECIMAL(15, 2),
    discount_percentage DECIMAL(5, 2) DEFAULT 0,
    is_on_sale BOOLEAN DEFAULT FALSE,
    sale_start_date TIMESTAMP NULL,
    sale_end_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (currency_id) REFERENCES currencies(id),
    UNIQUE KEY unique_product_currency (product_id, currency_id),
    INDEX idx_product_id (product_id),
    INDEX idx_currency_id (currency_id)
) ENGINE = InnoDB;
-- Product Attributes (e.g., Color, Size)
CREATE TABLE product_attributes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    attribute_name VARCHAR(100) NOT NULL,
    attribute_value VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
) ENGINE = InnoDB;
-- Product Reviews
CREATE TABLE product_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (
        rating BETWEEN 1 AND 5
    ),
    title VARCHAR(255),
    comment TEXT,
    is_verified_purchase BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_rating (rating)
) ENGINE = InnoDB;
-- ============================================
-- ORDER SERVICE DATABASE
-- ============================================
CREATE DATABASE IF NOT EXISTS ecommerce_order CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce_order;
-- Orders Table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    subtotal DECIMAL(15, 2) NOT NULL,
    tax_amount DECIMAL(15, 2) DEFAULT 0,
    shipping_amount DECIMAL(15, 2) DEFAULT 0,
    discount_amount DECIMAL(15, 2) DEFAULT 0,
    total_amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    -- PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    -- PENDING, COMPLETED, FAILED, REFUNDED
    shipping_address_id BIGINT NOT NULL,
    billing_address_id BIGINT NOT NULL,
    notes TEXT,
    cancelled_at TIMESTAMP NULL,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version INT DEFAULT 0,
    INDEX idx_order_number (order_number),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_payment_status (payment_status),
    INDEX idx_created_at (created_at)
) ENGINE = InnoDB;
-- Order Items Table
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_sku VARCHAR(100),
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    total_price DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    -- PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    INDEX idx_seller_id (seller_id)
) ENGINE = InnoDB;
-- Payments Table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    -- RAZORPAY, CARD, UPI, WALLET
    payment_gateway VARCHAR(50) DEFAULT 'RAZORPAY',
    razorpay_order_id VARCHAR(255),
    razorpay_payment_id VARCHAR(255),
    razorpay_signature VARCHAR(500),
    amount DECIMAL(15, 2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    -- PENDING, COMPLETED, FAILED, REFUNDED
    transaction_id VARCHAR(255),
    payment_date TIMESTAMP NULL,
    failure_reason TEXT,
    refund_amount DECIMAL(15, 2) DEFAULT 0,
    refund_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_razorpay_order_id (razorpay_order_id),
    INDEX idx_razorpay_payment_id (razorpay_payment_id),
    INDEX idx_status (status)
) ENGINE = InnoDB;
-- Order Status History
CREATE TABLE order_status_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    notes TEXT,
    changed_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id)
) ENGINE = InnoDB;
-- Shopping Cart
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id)
) ENGINE = InnoDB;
-- ============================================
-- ADMIN SERVICE DATABASE
-- ============================================
CREATE DATABASE IF NOT EXISTS ecommerce_admin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce_admin;
-- Platform Settings
CREATE TABLE platform_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    INDEX idx_key (setting_key)
) ENGINE = InnoDB;
-- Audit Logs
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_created_at (created_at)
) ENGINE = InnoDB;
-- ============================================
-- NOTIFICATION SERVICE DATABASE
-- ============================================
CREATE DATABASE IF NOT EXISTS ecommerce_notification CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce_notification;
-- Email Templates
CREATE TABLE email_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL UNIQUE,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    template_type VARCHAR(50),
    -- WELCOME, ORDER_CONFIRMATION, PASSWORD_RESET, etc.
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (template_name),
    INDEX idx_type (template_type)
) ENGINE = InnoDB;
-- Notification Queue
CREATE TABLE notification_queue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    recipient_email VARCHAR(255),
    recipient_phone VARCHAR(20),
    notification_type VARCHAR(50),
    -- EMAIL, SMS, WHATSAPP
    template_name VARCHAR(100),
    subject VARCHAR(255),
    message TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    -- PENDING, SENT, FAILED
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    error_message TEXT,
    sent_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE = InnoDB;
-- ============================================
-- INITIAL DATA SEEDING
-- ============================================
-- Insert Default Roles
USE ecommerce_user;
INSERT INTO roles (name, description)
VALUES (
        'ADMIN',
        'Platform administrator with full access'
    ),
    (
        'SELLER',
        'Seller who can manage their products and orders'
    ),
    (
        'CUSTOMER',
        'Regular customer who can browse and purchase'
    );
-- Insert Default Currencies
USE ecommerce_product;
INSERT INTO currencies (code, name, symbol, exchange_rate, is_active)
VALUES ('USD', 'US Dollar', '$', 1.000000, TRUE),
    ('EUR', 'Euro', '€', 0.850000, TRUE),
    ('GBP', 'British Pound', '£', 0.730000, TRUE),
    ('INR', 'Indian Rupee', '₹', 83.000000, TRUE),
    ('AUD', 'Australian Dollar', 'A$', 1.520000, TRUE),
    ('CAD', 'Canadian Dollar', 'C$', 1.350000, TRUE);
-- Insert Default Categories
USE ecommerce_product;
INSERT INTO categories (name, description, is_active, display_order)
VALUES (
        'Electronics',
        'Electronic devices and accessories',
        TRUE,
        1
    ),
    (
        'Fashion',
        'Clothing, shoes, and accessories',
        TRUE,
        2
    ),
    (
        'Home & Kitchen',
        'Home appliances and kitchen items',
        TRUE,
        3
    ),
    (
        'Books',
        'Books and educational materials',
        TRUE,
        4
    ),
    (
        'Sports & Outdoors',
        'Sports equipment and outdoor gear',
        TRUE,
        5
    ),
    (
        'Beauty & Personal Care',
        'Beauty products and personal care items',
        TRUE,
        6
    ),
    (
        'Toys & Games',
        'Toys, games, and hobbies',
        TRUE,
        7
    ),
    (
        'Health & Wellness',
        'Health supplements and wellness products',
        TRUE,
        8
    );
-- Insert Email Templates
USE ecommerce_notification;
INSERT INTO email_templates (
        template_name,
        subject,
        body,
        template_type,
        is_active
    )
VALUES (
        'WELCOME',
        'Welcome to Our E-Commerce Platform!',
        '<html><body><h1>Welcome {{firstName}}!</h1><p>Thank you for joining our platform.</p></body></html>',
        'WELCOME',
        TRUE
    ),
    (
        'ORDER_CONFIRMATION',
        'Order Confirmation - {{orderNumber}}',
        '<html><body><h1>Order Confirmed!</h1><p>Your order {{orderNumber}} has been confirmed.</p><p>Total: {{totalAmount}}</p></body></html>',
        'ORDER_CONFIRMATION',
        TRUE
    ),
    (
        'PASSWORD_RESET',
        'Password Reset Request',
        '<html><body><h1>Reset Your Password</h1><p>Click the link below to reset your password:</p><a href="{{resetLink}}">Reset Password</a></body></html>',
        'PASSWORD_RESET',
        TRUE
    ),
    (
        'SELLER_APPROVAL',
        'Seller Account Approved',
        '<html><body><h1>Congratulations!</h1><p>Your seller account has been approved. You can now start listing products.</p></body></html>',
        'SELLER_APPROVAL',
        TRUE
    );
-- Insert Platform Settings
USE ecommerce_admin;
INSERT INTO platform_settings (setting_key, setting_value, description)
VALUES (
        'PLATFORM_NAME',
        'E-Commerce Platform',
        'Name of the platform'
    ),
    (
        'SUPPORT_EMAIL',
        'support@ecommerce.com',
        'Support email address'
    ),
    (
        'BASE_CURRENCY',
        'USD',
        'Base currency for the platform'
    ),
    ('TAX_RATE', '0.18', 'Default tax rate (18%)'),
    ('SHIPPING_RATE', '5.00', 'Default shipping rate'),
    (
        'MIN_ORDER_AMOUNT',
        '10.00',
        'Minimum order amount'
    ),
    ('MAX_CART_ITEMS', '50', 'Maximum items in cart');
-- ============================================
-- INDEXES FOR PERFORMANCE OPTIMIZATION
-- ============================================
-- Additional composite indexes for common queries
USE ecommerce_product;
CREATE INDEX idx_product_active_category ON products(is_active, category_id);
CREATE INDEX idx_product_seller_status ON products(seller_id, approval_status);
USE ecommerce_order;
CREATE INDEX idx_order_user_status ON orders(user_id, status);
CREATE INDEX idx_order_user_date ON orders(user_id, created_at);
-- ============================================
-- END OF SCHEMA
-- ============================================