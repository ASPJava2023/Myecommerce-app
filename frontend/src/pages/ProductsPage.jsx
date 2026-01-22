import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import productService from '../services/productService';
import categoryService from '../services/categoryService';
import cartService from '../services/cartService';
import ProductCard from '../components/ProductCard';
import Pagination from '../components/Pagination';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import { useAuth } from '../context/AuthContext';
import './ProductsPage.css';

const ProductsPage = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();

    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [totalPages, setTotalPages] = useState(0);

    const currentPage = parseInt(searchParams.get('page') || '0');
    const searchQuery = searchParams.get('search') || '';
    const categoryId = searchParams.get('category') || '';

    useEffect(() => {
        loadCategories();
    }, []);

    useEffect(() => {
        loadProducts();
    }, [currentPage, searchQuery, categoryId]);

    const loadCategories = async () => {
        try {
            const data = await categoryService.getAllCategories();
            setCategories(data || []);
        } catch (err) {
            console.error('Failed to load categories:', err);
        }
    };

    const loadProducts = async () => {
        try {
            setLoading(true);
            setError(null);

            let data;
            if (searchQuery) {
                data = await productService.searchProducts(searchQuery, currentPage);
            } else if (categoryId) {
                data = await productService.getProductsByCategory(categoryId, currentPage);
            } else {
                data = await productService.getAllProducts(currentPage);
            }

            setProducts(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handlePageChange = (newPage) => {
        searchParams.set('page', newPage.toString());
        setSearchParams(searchParams);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const handleCategoryFilter = (catId) => {
        const newParams = new URLSearchParams();
        if (catId) {
            newParams.set('category', catId);
        }
        setSearchParams(newParams);
    };

    const handleAddToCart = async (product) => {
        if (!isAuthenticated) {
            navigate('/login');
            return;
        }

        try {
            await cartService.addToCart({
                productId: product.id,
                quantity: 1,
            });
            alert('Product added to cart!');
        } catch (err) {
            alert('Failed to add to cart: ' + err.message);
        }
    };

    if (loading) return <Loading />;
    if (error) return <ErrorMessage message={error} onRetry={loadProducts} />;

    return (
        <div className="products-page">
            <div className="container">
                <div className="products-layout">
                    {/* Sidebar */}
                    <aside className="products-sidebar">
                        <div className="glass-card">
                            <h3>Categories</h3>
                            <div className="category-filters">
                                <button
                                    className={`category-filter-btn ${!categoryId ? 'active' : ''}`}
                                    onClick={() => handleCategoryFilter('')}
                                >
                                    All Products
                                </button>
                                {categories.map((category) => (
                                    <button
                                        key={category.id}
                                        className={`category-filter-btn ${categoryId === category.id.toString() ? 'active' : ''}`}
                                        onClick={() => handleCategoryFilter(category.id.toString())}
                                    >
                                        {category.name}
                                    </button>
                                ))}
                            </div>
                        </div>
                    </aside>

                    {/* Main Content */}
                    <main className="products-main">
                        <div className="products-header">
                            <h1>
                                {searchQuery ? `Search Results for "${searchQuery}"` :
                                    categoryId ? categories.find(c => c.id.toString() === categoryId)?.name || 'Products' :
                                        'All Products'}
                            </h1>
                            <p className="products-count">{products.length} products found</p>
                        </div>

                        {products.length === 0 ? (
                            <div className="no-products">
                                <p>No products found</p>
                            </div>
                        ) : (
                            <>
                                <div className="products-grid">
                                    {products.map((product) => (
                                        <ProductCard
                                            key={product.id}
                                            product={product}
                                            onAddToCart={handleAddToCart}
                                        />
                                    ))}
                                </div>

                                <Pagination
                                    currentPage={currentPage}
                                    totalPages={totalPages}
                                    onPageChange={handlePageChange}
                                />
                            </>
                        )}
                    </main>
                </div>
            </div>
        </div>
    );
};

export default ProductsPage;
