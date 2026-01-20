-- Insert default roles if they don't exist
INSERT IGNORE INTO roles (id, name, description, created_at, updated_at)
VALUES (
        1,
        'ADMIN',
        'Platform administrator with full access',
        NOW(),
        NOW()
    ),
    (
        2,
        'SELLER',
        'Seller who can manage their products and orders',
        NOW(),
        NOW()
    ),
    (
        3,
        'CUSTOMER',
        'Regular customer who can browse and purchase',
        NOW(),
        NOW()
    );