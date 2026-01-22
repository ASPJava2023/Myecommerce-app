// API Configuration
export const API_CONFIG = {
    BASE_URL: '/api', // Proxied through Vite to API Gateway at localhost:8080
    TIMEOUT: 30000,
};

// API Endpoints
export const ENDPOINTS = {
    // Auth
    AUTH: {
        REGISTER: '/auth/register',
        LOGIN: '/auth/login',
        LOGOUT: '/auth/logout',
        ME: '/auth/me',
    },

    // Products
    PRODUCTS: {
        BASE: '/products',
        BY_ID: (id) => `/products/${id}`,
        SEARCH: '/products/search',
        BY_CATEGORY: (categoryId) => `/products/category/${categoryId}`,
        FEATURED: '/products/featured',
    },

    // Categories
    CATEGORIES: {
        BASE: '/categories',
        BY_ID: (id) => `/categories/${id}`,
    },

    // Cart
    CART: {
        BASE: '/cart',
        ITEMS: '/cart/items',
        ITEM_BY_ID: (id) => `/cart/items/${id}`,
    },

    // Orders
    ORDERS: {
        BASE: '/orders',
        BY_ID: (id) => `/orders/${id}`,
        CANCEL: (id) => `/orders/${id}/cancel`,
    },
};

// Local Storage Keys
export const STORAGE_KEYS = {
    TOKEN: 'auth_token',
    USER: 'user_data',
};

// Order Status
export const ORDER_STATUS = {
    PENDING: 'PENDING',
    CONFIRMED: 'CONFIRMED',
    PROCESSING: 'PROCESSING',
    SHIPPED: 'SHIPPED',
    DELIVERED: 'DELIVERED',
    CANCELLED: 'CANCELLED',
};

// Pagination
export const PAGINATION = {
    DEFAULT_PAGE: 0,
    DEFAULT_SIZE: 12,
};
