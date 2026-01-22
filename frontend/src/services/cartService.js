import api from './api';
import { ENDPOINTS } from '../config/constants';

const cartService = {
    /**
     * Get current user's cart
     * @returns {Promise} Cart with items
     */
    getCart: async () => {
        const response = await api.get(ENDPOINTS.CART.BASE);
        return response.data;
    },

    /**
     * Add item to cart
     * @param {Object} cartItem - Product ID and quantity
     * @returns {Promise} Added cart item
     */
    addToCart: async (cartItem) => {
        const response = await api.post(ENDPOINTS.CART.ITEMS, cartItem);
        return response.data;
    },

    /**
     * Update cart item quantity
     * @param {number} itemId - Cart item ID
     * @param {number} quantity - New quantity
     * @returns {Promise} Updated cart item
     */
    updateCartItem: async (itemId, quantity) => {
        const response = await api.put(ENDPOINTS.CART.ITEM_BY_ID(itemId), null, {
            params: { quantity },
        });
        return response.data;
    },

    /**
     * Remove item from cart
     * @param {number} itemId - Cart item ID
     * @returns {Promise} Success response
     */
    removeFromCart: async (itemId) => {
        const response = await api.delete(ENDPOINTS.CART.ITEM_BY_ID(itemId));
        return response.data;
    },

    /**
     * Clear entire cart
     * @returns {Promise} Success response
     */
    clearCart: async () => {
        const response = await api.delete(ENDPOINTS.CART.BASE);
        return response.data;
    },
};

export default cartService;
