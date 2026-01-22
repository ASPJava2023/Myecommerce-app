import { createContext, useContext, useState, useEffect } from 'react';
import authService from '../services/authService';

const AuthContext = createContext(null);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    // Initialize auth state from localStorage
    useEffect(() => {
        const initAuth = () => {
            const token = authService.getToken();
            const userData = authService.getUser();

            if (token && userData) {
                setUser(userData);
                setIsAuthenticated(true);
            }

            setLoading(false);
        };

        initAuth();
    }, []);

    /**
     * Login user
     */
    const login = async (credentials) => {
        try {
            const response = await authService.login(credentials);
            setUser(response.user);
            setIsAuthenticated(true);
            return response;
        } catch (error) {
            throw error;
        }
    };

    /**
     * Register new user
     */
    const register = async (userData) => {
        try {
            const response = await authService.register(userData);
            // Auto-login after registration
            if (response) {
                // Registration successful, now login
                await login({
                    email: userData.email,
                    password: userData.password,
                });
            }
            return response;
        } catch (error) {
            throw error;
        }
    };

    /**
     * Logout user
     */
    const logout = async () => {
        try {
            await authService.logout();
        } finally {
            setUser(null);
            setIsAuthenticated(false);
        }
    };

    /**
     * Update user data
     */
    const updateUser = (userData) => {
        setUser(userData);
        localStorage.setItem('user_data', JSON.stringify(userData));
    };

    const value = {
        user,
        isAuthenticated,
        loading,
        login,
        register,
        logout,
        updateUser,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
