import api from './api';
import { ENDPOINTS } from '../config/constants';

const categoryService = {
    /**
     * Get all categories
     * @returns {Promise} List of categories
     */
    getAllCategories: async () => {
        const response = await api.get(ENDPOINTS.CATEGORIES.BASE);
        return response.data;
    },

    /**
     * Get category by ID
     * @param {number} id - Category ID
     * @returns {Promise} Category details
     */
    getCategoryById: async (id) => {
        const response = await api.get(ENDPOINTS.CATEGORIES.BY_ID(id));
        return response.data;
    },
};

export default categoryService;
