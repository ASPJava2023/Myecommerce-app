import axios from 'axios';

const API_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const authService = {
    login: async (email, password) => {
        try {
            const response = await axios.post(`${API_URL}/auth/login`, {
                email,
                password
            });

            if (response.data.data && response.data.data.accessToken) {
                localStorage.setItem('adminToken', response.data.data.accessToken);
                localStorage.setItem('adminUser', JSON.stringify(response.data.data.user));
                return response.data;
            }
            throw new Error('Invalid response format');
        } catch (error) {
            throw error.response?.data?.message || 'Login failed';
        }
    },

    logout: () => {
        localStorage.removeItem('adminToken');
        localStorage.removeItem('adminUser');
    },

    getCurrentUser: () => {
        const user = localStorage.getItem('adminUser');
        return user ? JSON.parse(user) : null;
    },

    getToken: () => {
        return localStorage.getItem('adminToken');
    },

    isAuthenticated: () => {
        return !!localStorage.getItem('adminToken');
    }
};

export default authService;
