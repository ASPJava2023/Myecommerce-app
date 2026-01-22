import api from './api';
import { ENDPOINTS, PAGINATION } from '../config/constants';

const orderService = {
    /**
     * Create a new order
     * @param {Object} orderData - Order details (address, payment method)
     * @returns {Promise} Created order
     */
    createOrder: async (orderData) => {
        const response = await api.post(ENDPOINTS.ORDERS.BASE, orderData);
        return response.data;
    },

    /**
     * Get order by ID
     * @param {number} orderId - Order ID
     * @returns {Promise} Order details
     */
    getOrderById: async (orderId) => {
        const response = await api.get(ENDPOINTS.ORDERS.BY_ID(orderId));
        return response.data;
    },

    /**
     * Get user's orders with pagination
     * @param {number} page - Page number
     * @param {number} size - Page size
     * @param {string} sortBy - Sort field
     * @param {string} sortDir - Sort direction (ASC/DESC)
     * @returns {Promise} Paged orders response
     */
    getUserOrders: async (
        page = PAGINATION.DEFAULT_PAGE,
        size = PAGINATION.DEFAULT_SIZE,
        sortBy = 'orderDate',
        sortDir = 'DESC'
    ) => {
        const response = await api.get(ENDPOINTS.ORDERS.BASE, {
            params: { page, size, sortBy, sortDir },
        });
        return response.data;
    },

    /**
     * Cancel an order
     * @param {number} orderId - Order ID
     * @returns {Promise} Cancelled order
     */
    cancelOrder: async (orderId) => {
        const response = await api.put(ENDPOINTS.ORDERS.CANCEL(orderId));
        return response.data;
    },
};

export default orderService;
