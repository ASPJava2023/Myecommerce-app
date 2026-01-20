# Database Entity Relationship Diagram

## Overview

This document describes the database schema and relationships for the E-Commerce Platform microservices architecture.

## Database Architecture

The system uses **5 separate databases** for different microservices:

1. **ecommerce_user** - User Service
2. **ecommerce_seller** - Seller Service  
3. **ecommerce_product** - Product Service
4. **ecommerce_order** - Order Service
5. **ecommerce_admin** - Admin Service
6. **ecommerce_notification** - Notification Service

## Entity Relationship Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           USER SERVICE DATABASE                              │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────────────┐
    │      USERS       │
    ├──────────────────┤
    │ PK id            │
    │    email         │◄──────────┐
    │    password      │           │
    │    first_name    │           │
    │    last_name     │           │
    │    phone_number  │           │
    │    oauth_provider│           │
    │    status        │           │
    │    version       │           │
    └────────┬─────────┘           │
             │                     │
             │ 1                   │
             │                     │
             │ N                   │
    ┌────────▼─────────┐           │
    │   USER_ROLES     │           │
    ├──────────────────┤           │
    │ PK user_id       │           │
    │ PK role_id       │           │
    └────────┬─────────┘           │
             │                     │
             │ N                   │
             │                     │
             │ 1                   │
    ┌────────▼─────────┐           │
    │      ROLES       │           │
    ├──────────────────┤           │
    │ PK id            │           │
    │    name          │           │
    │    description   │           │
    └──────────────────┘           │
                                   │
    ┌──────────────────┐           │
    │    ADDRESSES     │           │
    ├──────────────────┤           │
    │ PK id            │           │
    │ FK user_id       ├───────────┘
    │    address_type  │
    │    full_name     │
    │    phone_number  │
    │    address_line1 │
    │    city          │
    │    state         │
    │    country       │
    │    postal_code   │
    │    is_default    │
    └──────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                         SELLER SERVICE DATABASE                              │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────────────┐
    │     SELLERS      │
    ├──────────────────┤
    │ PK id            │
    │ FK user_id       │ (References users.id)
    │    business_name │
    │    business_email│
    │    tax_id        │
    │    status        │
    │    approved_by   │
    │    version       │
    └────────┬─────────┘
             │
             │ 1
             │
             │ N
    ┌────────▼─────────┐
    │ SELLER_PRODUCTS  │
    ├──────────────────┤
    │ PK seller_id     │
    │ PK product_id    │ (References products.id)
    │    stock_quantity│
    │    is_active     │
    └──────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                        PRODUCT SERVICE DATABASE                              │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────────────┐
    │   CATEGORIES     │
    ├──────────────────┤
    │ PK id            │
    │    name          │
    │ FK parent_cat_id │ (Self-reference)
    │    is_active     │
    └────────┬─────────┘
             │
             │ 1
             │
             │ N
    ┌────────▼─────────┐         ┌──────────────────┐
    │    PRODUCTS      │         │  PRODUCT_IMAGES  │
    ├──────────────────┤         ├──────────────────┤
    │ PK id            │◄────1:N─┤ PK id            │
    │ FK seller_id     │         │ FK product_id    │
    │ FK category_id   │         │    image_url     │
    │    name          │         │    is_primary    │
    │    description   │         └──────────────────┘
    │    sku           │
    │    approval_stat │         ┌──────────────────┐
    │    version       │         │ PRODUCT_PRICES   │
    └────────┬─────────┘         ├──────────────────┤
             │                   │ PK id            │
             │ 1                 │ FK product_id    │
             │                   │ FK currency_id   │
             │ N                 │    base_price    │
    ┌────────▼─────────┐         │    sale_price    │
    │PRODUCT_ATTRIBUTES│         │    is_on_sale    │
    ├──────────────────┤         └────────┬─────────┘
    │ PK id            │                  │
    │ FK product_id    │                  │ N
    │    attr_name     │                  │
    │    attr_value    │                  │ 1
    └──────────────────┘         ┌────────▼─────────┐
                                 │   CURRENCIES     │
    ┌──────────────────┐         ├──────────────────┤
    │ PRODUCT_REVIEWS  │         │ PK id            │
    ├──────────────────┤         │    code (USD)    │
    │ PK id            │         │    name          │
    │ FK product_id    │         │    symbol        │
    │ FK user_id       │         │    exchange_rate │
    │    rating        │         └──────────────────┘
    │    comment       │
    │    is_verified   │
    └──────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                          ORDER SERVICE DATABASE                              │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────────────┐
    │     ORDERS       │
    ├──────────────────┤
    │ PK id            │
    │    order_number  │
    │ FK user_id       │ (References users.id)
    │ FK currency_id   │
    │    subtotal      │
    │    total_amount  │
    │    status        │
    │    payment_status│
    │ FK shipping_addr │
    │ FK billing_addr  │
    │    version       │
    └────────┬─────────┘
             │
             │ 1
             │
             ├──────────────────────┐
             │                      │
             │ N                    │ N
    ┌────────▼─────────┐   ┌────────▼─────────┐
    │  ORDER_ITEMS     │   │    PAYMENTS      │
    ├──────────────────┤   ├──────────────────┤
    │ PK id            │   │ PK id            │
    │ FK order_id      │   │ FK order_id      │
    │ FK product_id    │   │    payment_method│
    │ FK seller_id     │   │    razorpay_id   │
    │    product_name  │   │    amount        │
    │    quantity      │   │    status        │
    │    unit_price    │   │    transaction_id│
    │    total_price   │   └──────────────────┘
    │    status        │
    └──────────────────┘   ┌──────────────────┐
                           │ORDER_STATUS_HIST │
    ┌──────────────────┐   ├──────────────────┤
    │   CART_ITEMS     │   │ PK id            │
    ├──────────────────┤   │ FK order_id      │
    │ PK id            │   │    old_status    │
    │ FK user_id       │   │    new_status    │
    │ FK product_id    │   │    notes         │
    │    quantity      │   │    changed_by    │
    └──────────────────┘   └──────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                         ADMIN SERVICE DATABASE                               │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────────────┐
    │PLATFORM_SETTINGS │
    ├──────────────────┤
    │ PK id            │
    │    setting_key   │
    │    setting_value │
    │    description   │
    └──────────────────┘

    ┌──────────────────┐
    │   AUDIT_LOGS     │
    ├──────────────────┤
    │ PK id            │
    │ FK user_id       │
    │    action        │
    │    entity_type   │
    │    entity_id     │
    │    old_value     │
    │    new_value     │
    │    ip_address    │
    └──────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                      NOTIFICATION SERVICE DATABASE                           │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────────────┐
    │ EMAIL_TEMPLATES  │
    ├──────────────────┤
    │ PK id            │
    │    template_name │
    │    subject       │
    │    body          │
    │    template_type │
    └──────────────────┘

    ┌──────────────────┐
    │NOTIFICATION_QUEUE│
    ├──────────────────┤
    │ PK id            │
    │ FK user_id       │
    │    recipient     │
    │    type          │
    │    message       │
    │    status        │
    │    retry_count   │
    └──────────────────┘
```

## Cross-Service Relationships

While each microservice has its own database, there are logical relationships across services:

### User → Seller
- `sellers.user_id` references `users.id` (via API, not FK)

### Seller → Product
- `products.seller_id` references `sellers.id` (via API)
- `seller_products` maps sellers to products

### Order → User
- `orders.user_id` references `users.id` (via API)

### Order → Product
- `order_items.product_id` references `products.id` (via API)

### Order → Seller
- `order_items.seller_id` references `sellers.id` (via API)

## Key Design Decisions

### 1. Database Per Service
Each microservice has its own database to ensure:
- **Loose coupling**: Services can evolve independently
- **Scalability**: Each database can be scaled separately
- **Fault isolation**: Database issues don't cascade

### 2. No Foreign Keys Across Services
- Cross-service references are maintained via API calls
- Data consistency handled at application level
- Eventual consistency model for distributed data

### 3. Optimistic Locking
- `version` column in critical tables
- Prevents lost updates in concurrent scenarios
- Implemented using JPA `@Version`

### 4. Audit Fields
All tables include:
- `created_at`: Record creation timestamp
- `updated_at`: Last update timestamp
- `created_by`: User who created the record
- `updated_by`: User who last updated

### 5. Soft Deletes
- Most entities use status flags instead of hard deletes
- Preserves data for audit and analytics
- Examples: `is_active`, `status`

### 6. Indexes
Strategic indexes on:
- Foreign keys
- Frequently queried columns
- Composite indexes for common query patterns
- Full-text indexes for search

## Table Descriptions

### User Service

| Table | Purpose |
|-------|---------|
| users | Core user accounts with OAuth support |
| roles | Role definitions (ADMIN, SELLER, CUSTOMER) |
| user_roles | Many-to-many user-role mapping |
| addresses | User shipping/billing addresses |
| password_reset_tokens | Password reset functionality |
| refresh_tokens | JWT refresh token management |

### Seller Service

| Table | Purpose |
|-------|---------|
| sellers | Seller business information |
| seller_products | Seller-product ownership and stock |

### Product Service

| Table | Purpose |
|-------|---------|
| categories | Hierarchical product categories |
| products | Product catalog with approval workflow |
| product_images | Product image URLs (S3) |
| currencies | Supported currencies |
| product_prices | Multi-currency pricing |
| product_attributes | Dynamic product attributes |
| product_reviews | Customer reviews and ratings |

### Order Service

| Table | Purpose |
|-------|---------|
| orders | Order header information |
| order_items | Order line items (multi-seller support) |
| payments | Razorpay payment transactions |
| order_status_history | Order status audit trail |
| cart_items | Shopping cart (pre-order) |

### Admin Service

| Table | Purpose |
|-------|---------|
| platform_settings | Configurable platform settings |
| audit_logs | System-wide audit trail |

### Notification Service

| Table | Purpose |
|-------|---------|
| email_templates | Reusable email templates |
| notification_queue | Async notification queue |

## Data Flow Examples

### 1. User Registration
```
1. Create record in users table
2. Assign CUSTOMER role in user_roles
3. Trigger welcome email via notification_queue
```

### 2. Seller Onboarding
```
1. User exists in users table
2. Create seller record in sellers table
3. Admin approves (status = APPROVED)
4. Assign SELLER role in user_roles
5. Trigger approval email
```

### 3. Product Creation
```
1. Seller creates product in products table
2. Upload images to S3, store URLs in product_images
3. Set prices in product_prices for each currency
4. Admin approves (approval_status = APPROVED)
5. Create seller_products mapping
```

### 4. Order Placement
```
1. Create order in orders table
2. Create order_items for each cart item
3. Create payment record with Razorpay
4. Update order status via order_status_history
5. Trigger order confirmation email
6. Clear cart_items
```

## Performance Considerations

### Indexing Strategy
- **Primary Keys**: Auto-increment BIGINT for scalability
- **Foreign Keys**: Indexed for join performance
- **Status Columns**: Indexed for filtering
- **Timestamps**: Indexed for date-range queries
- **Composite Indexes**: For multi-column filters

### Partitioning (Future)
- Orders table by date (monthly partitions)
- Audit logs by date (monthly partitions)
- Notification queue by status

### Caching Strategy
- Product catalog (Redis)
- User sessions (Redis)
- Currency exchange rates (Redis)
- Category tree (Redis)

## Security Considerations

1. **Password Storage**: BCrypt hashing
2. **Sensitive Data**: Encrypted at rest (AWS RDS encryption)
3. **PII Protection**: GDPR-compliant data handling
4. **Audit Trail**: All critical operations logged
5. **Row-Level Security**: User can only access their data

## Backup & Recovery

- **RDS Automated Backups**: Daily snapshots
- **Point-in-Time Recovery**: 7-day retention
- **Multi-AZ Deployment**: High availability
- **Read Replicas**: For reporting and analytics

---

**Last Updated**: 2026-01-20
**Version**: 1.0
