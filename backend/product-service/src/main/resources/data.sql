-- Insert default categories
INSERT IGNORE INTO categories (
        id,
        name,
        description,
        is_active,
        display_order,
        created_at,
        updated_at
    )
VALUES (
        1,
        'Electronics',
        'Electronic devices and accessories',
        true,
        1,
        NOW(),
        NOW()
    ),
    (
        2,
        'Fashion',
        'Clothing, shoes, and accessories',
        true,
        2,
        NOW(),
        NOW()
    ),
    (
        3,
        'Home & Kitchen',
        'Home appliances and kitchen items',
        true,
        3,
        NOW(),
        NOW()
    ),
    (
        4,
        'Books',
        'Books and educational materials',
        true,
        4,
        NOW(),
        NOW()
    ),
    (
        5,
        'Sports & Outdoors',
        'Sports equipment and outdoor gear',
        true,
        5,
        NOW(),
        NOW()
    );