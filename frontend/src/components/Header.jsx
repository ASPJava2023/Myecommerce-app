import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './Header.css';

const Header = () => {
    const { isAuthenticated, user, logout } = useAuth();
    const [searchQuery, setSearchQuery] = useState('');
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const navigate = useNavigate();

    const handleSearch = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            navigate(`/products?search=${encodeURIComponent(searchQuery)}`);
            setSearchQuery('');
        }
    };

    const handleLogout = async () => {
        await logout();
        navigate('/');
    };

    return (
        <header className="header">
            <div className="container">
                <div className="header-content">
                    {/* Logo */}
                    <Link to="/" className="logo">
                        <span className="logo-icon">🛍️</span>
                        <span className="logo-text">ShopSphere</span>
                    </Link>

                    {/* Search Bar */}
                    <form className="search-form" onSubmit={handleSearch}>
                        <input
                            type="text"
                            className="search-input"
                            placeholder="Search products..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                        <button type="submit" className="search-btn">
                            🔍
                        </button>
                    </form>

                    {/* Desktop Navigation */}
                    <nav className="nav-desktop">
                        <Link to="/products" className="nav-link">Products</Link>

                        {isAuthenticated ? (
                            <>
                                <Link to="/cart" className="nav-link cart-link">
                                    🛒 Cart
                                </Link>
                                <Link to="/orders" className="nav-link">Orders</Link>
                                <div className="user-menu">
                                    <span className="user-name">👤 {user?.firstName || 'User'}</span>
                                    <button onClick={handleLogout} className="btn btn-sm btn-secondary">
                                        Logout
                                    </button>
                                </div>
                            </>
                        ) : (
                            <>
                                <Link to="/login" className="btn btn-sm btn-secondary">Login</Link>
                                <Link to="/register" className="btn btn-sm btn-primary">Sign Up</Link>
                            </>
                        )}
                    </nav>

                    {/* Mobile Menu Toggle */}
                    <button
                        className="mobile-menu-toggle"
                        onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                    >
                        {mobileMenuOpen ? '✕' : '☰'}
                    </button>
                </div>

                {/* Mobile Navigation */}
                {mobileMenuOpen && (
                    <nav className="nav-mobile">
                        <Link to="/products" className="nav-link-mobile" onClick={() => setMobileMenuOpen(false)}>
                            Products
                        </Link>
                        {isAuthenticated ? (
                            <>
                                <Link to="/cart" className="nav-link-mobile" onClick={() => setMobileMenuOpen(false)}>
                                    🛒 Cart
                                </Link>
                                <Link to="/orders" className="nav-link-mobile" onClick={() => setMobileMenuOpen(false)}>
                                    Orders
                                </Link>
                                <button onClick={() => { handleLogout(); setMobileMenuOpen(false); }} className="btn btn-secondary" style={{ width: '100%' }}>
                                    Logout
                                </button>
                            </>
                        ) : (
                            <>
                                <Link to="/login" className="btn btn-secondary" onClick={() => setMobileMenuOpen(false)} style={{ width: '100%' }}>
                                    Login
                                </Link>
                                <Link to="/register" className="btn btn-primary" onClick={() => setMobileMenuOpen(false)} style={{ width: '100%', marginTop: 'var(--space-sm)' }}>
                                    Sign Up
                                </Link>
                            </>
                        )}
                    </nav>
                )}
            </div>
        </header>
    );
};

export default Header;
