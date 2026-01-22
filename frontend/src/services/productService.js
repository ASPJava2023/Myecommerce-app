import api from './api';
import { ENDPOINTS, PAGINATION } from '../config/constants';

const productService = {
    /**
     * Get all products with pagination
     * @param {number} page - Page number
     * @param {number} size - Page size
     * @param {string} sortBy - Sort field
     * @returns {Promise} Paged product response
     */
    getAllProducts: async (page = PAGINATION.DEFAULT_PAGE, size = PAGINATION.DEFAULT_SIZE, sortBy = 'id') => {
        const response = await api.get(ENDPOINTS.PRODUCTS.BASE, {
            params: { page, size, sortBy },
        });
        return response.data;
    },

    /**
     * Get product by ID
     * @param {number} id - Product ID
     * @returns {Promise} Product details
     */
    getProductById: async (id) => {
        const response = await api.get(ENDPOINTS.PRODUCTS.BY_ID(id));
        return response.data;
    },

    /**
     * Search products
     * @param {string} query - Search query
     * @param {number} page - Page number
     * @param {number} size - Page size
     * @returns {Promise} Paged product response
     */
    searchProducts: async (query, page = PAGINATION.DEFAULT_PAGE, size = PAGINATION.DEFAULT_SIZE) => {
        const response = await api.get(ENDPOINTS.PRODUCTS.SEARCH, {
            params: { q: query, page, size },
        });
        return response.data;
    },

    /**
     * Get products by category
     * @param {number} categoryId - Category ID
     * @param {number} page - Page number
     * @param {number} size - Page size
     * @returns {Promise} Paged product response
     */
    getProductsByCategory: async (categoryId, page = PAGINATION.DEFAULT_PAGE, size = PAGINATION.DEFAULT_SIZE) => {
        const response = await api.get(ENDPOINTS.PRODUCTS.BY_CATEGORY(categoryId), {
            params: { page, size },
        });
        return response.data;
    },

    /**
     * Get featured products
     * @param {number} page - Page number
     * @param {number} size - Page size
     * @returns {Promise} Paged product response
     */
    getFeaturedProducts: async (page = PAGINATION.DEFAULT_PAGE, size = 8) => {
        const response = await api.get(ENDPOINTS.PRODUCTS.FEATURED, {
            params: { page, size },
        });
        return response.data;
    },
};

export default productService;
