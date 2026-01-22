import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import productService from '../services/productService';
import categoryService from '../services/categoryService';
import ProductCard from '../components/ProductCard';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import './HomePage.css';

const HomePage = () => {
    const [featuredProducts, setFeaturedProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            setLoading(true);
            setError(null);

            const [productsData, categoriesData] = await Promise.all([
                productService.getFeaturedProducts(0, 8),
                categoryService.getAllCategories(),
            ]);

            setFeaturedProducts(productsData.content || []);
            setCategories(categoriesData || []);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Loading />;
    if (error) return <ErrorMessage message={error} onRetry={loadData} />;

    return (
        <div className="home-page">
            {/* Hero Section */}
            <section className="hero-section">
                <div className="container">
                    <div className="hero-content">
                        <h1 className="hero-title">
                            Welcome to <span className="gradient-text">ShopSphere</span>
                        </h1>
                        <p className="hero-subtitle">
                            Discover amazing products with seamless checkout and lightning-fast delivery
                        </p>
                        <div className="hero-actions">
                            <Link to="/products" className="btn btn-lg btn-primary">
                                Shop Now
                            </Link>
                            <Link to="/products?featured=true" className="btn btn-lg btn-secondary">
                                Featured Products
                            </Link>
                        </div>
                    </div>
                </div>
            </section>

            {/* Categories Section */}
            {categories.length > 0 && (
                <section className="categories-section">
                    <div className="container">
                        <h2 className="section-title">Shop by Category</h2>
                        <div className="categories-grid">
                            {categories.slice(0, 6).map((category) => (
                                <Link
                                    key={category.id}
                                    to={`/products?category=${category.id}`}
                                    className="category-card glass-card"
                                >
                                    <div className="category-icon">📦</div>
                                    <h3>{category.name}</h3>
                                    <p>{category.description}</p>
                                </Link>
                            ))}
                        </div>
                    </div>
                </section>
            )}

            {/* Featured Products Section */}
            {featuredProducts.length > 0 && (
                <section className="featured-section">
                    <div className="container">
                        <div className="section-header">
                            <h2 className="section-title">Featured Products</h2>
                            <Link to="/products" className="view-all-link">
                                View All →
                            </Link>
                        </div>
                        <div className="products-grid">
                            {featuredProducts.map((product) => (
                                <ProductCard key={product.id} product={product} />
                            ))}
                        </div>
                    </div>
                </section>
            )}

            {/* Features Section */}
            <section className="features-section">
                <div className="container">
                    <div className="features-grid">
                        <div className="feature-card glass-card">
                            <div className="feature-icon">🚚</div>
                            <h3>Fast Delivery</h3>
                            <p>Get your orders delivered quickly and safely</p>
                        </div>
                        <div className="feature-card glass-card">
                            <div className="feature-icon">🔒</div>
                            <h3>Secure Payment</h3>
                            <p>100% secure payment with Razorpay integration</p>
                        </div>
                        <div className="feature-card glass-card">
                            <div className="feature-icon">💎</div>
                            <h3>Premium Quality</h3>
                            <p>Only the best products from trusted sellers</p>
                        </div>
                        <div className="feature-card glass-card">
                            <div className="feature-icon">🎁</div>
                            <h3>Easy Returns</h3>
                            <p>Hassle-free returns within 30 days</p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default HomePage;
