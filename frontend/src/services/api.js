import axios from 'axios';
import { API_CONFIG, STORAGE_KEYS } from '../config/constants';

// Create axios instance
const api = axios.create({
    baseURL: API_CONFIG.BASE_URL,
    timeout: API_CONFIG.TIMEOUT,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor - Add JWT token to requests
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor - Handle errors globally
api.interceptors.response.use(
    (response) => {
        // Extract data from the ApiResponse wrapper
        return response.data;
    },
    (error) => {
        if (error.response) {
            // Server responded with error
            const { status, data } = error.response;

            // Handle 401 Unauthorized - redirect to login
            if (status === 401) {
                localStorage.removeItem(STORAGE_KEYS.TOKEN);
                localStorage.removeItem(STORAGE_KEYS.USER);
                window.location.href = '/login';
            }

            // Extract error message from ApiResponse
            const errorMessage = data?.message || data?.error || 'An error occurred';
            return Promise.reject(new Error(errorMessage));
        } else if (error.request) {
            // Request made but no response
            return Promise.reject(new Error('No response from server. Please check your connection.'));
        } else {
            // Something else happened
            return Promise.reject(error);
        }
    }
);

export default api;
