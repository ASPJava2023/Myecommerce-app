import api from './api';
import { ENDPOINTS, STORAGE_KEYS } from '../config/constants';

const authService = {
    /**
     * Register a new user
     * @param {Object} userData - User registration data
     * @returns {Promise} User response
     */
    register: async (userData) => {
        const response = await api.post(ENDPOINTS.AUTH.REGISTER, userData);
        return response.data;
    },

    /**
     * Login user
     * @param {Object} credentials - Email and password
     * @returns {Promise} JWT response with token and user data
     */
    login: async (credentials) => {
        const response = await api.post(ENDPOINTS.AUTH.LOGIN, credentials);
        const { token, user } = response.data;

        // Store token and user data
        localStorage.setItem(STORAGE_KEYS.TOKEN, token);
        localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(user));

        return response.data;
    },

    /**
     * Logout user
     */
    logout: async () => {
        try {
            await api.post(ENDPOINTS.AUTH.LOGOUT);
        } finally {
            // Clear local storage regardless of API response
            localStorage.removeItem(STORAGE_KEYS.TOKEN);
            localStorage.removeItem(STORAGE_KEYS.USER);
        }
    },

    /**
     * Get current user info
     * @returns {Promise} Current user data
     */
    getCurrentUser: async () => {
        const response = await api.get(ENDPOINTS.AUTH.ME);
        return response.data;
    },

    /**
     * Get stored token
     * @returns {string|null} JWT token
     */
    getToken: () => {
        return localStorage.getItem(STORAGE_KEYS.TOKEN);
    },

    /**
     * Get stored user data
     * @returns {Object|null} User data
     */
    getUser: () => {
        const userData = localStorage.getItem(STORAGE_KEYS.USER);
        return userData ? JSON.parse(userData) : null;
    },

    /**
     * Check if user is authenticated
     * @returns {boolean} Authentication status
     */
    isAuthenticated: () => {
        return !!localStorage.getItem(STORAGE_KEYS.TOKEN);
    },
};

export default authService;
