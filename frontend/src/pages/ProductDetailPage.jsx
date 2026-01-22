import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import productService from '../services/productService';
import cartService from '../services/cartService';
import { useAuth } from '../context/AuthContext';
import { formatCurrency } from '../utils/formatters';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import './ProductDetailPage.css';

const ProductDetailPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();

    const [product, setProduct] = useState(null);
    const [quantity, setQuantity] = useState(1);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [adding, setAdding] = useState(false);

    useEffect(() => {
        loadProduct();
    }, [id]);

    const loadProduct = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await productService.getProductById(id);
            setProduct(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleAddToCart = async () => {
        if (!isAuthenticated) {
            navigate('/login');
            return;
        }

        try {
            setAdding(true);
            await cartService.addToCart({
                productId: product.id,
                quantity: quantity,
            });
            alert('Product added to cart!');
        } catch (err) {
            alert('Failed to add to cart: ' + err.message);
        } finally {
            setAdding(false);
        }
    };

    if (loading) return <Loading />;
    if (error) return <ErrorMessage message={error} onRetry={loadProduct} />;
    if (!product) return <ErrorMessage message="Product not found" />;

    return (
        <div className="product-detail-page">
            <div className="container">
                <div className="product-detail-layout">
                    {/* Product Image */}
                    <div className="product-image-section">
                        {product.imageUrl ? (
                            <img src={product.imageUrl} alt={product.name} className="product-detail-image" />
                        ) : (
                            <div className="product-detail-placeholder">
                                <span className="placeholder-icon">📦</span>
                            </div>
                        )}
                    </div>

                    {/* Product Info */}
                    <div className="product-info-section glass-card">
                        <div className="product-badges">
                            {product.featured && <span className="badge badge-info">⭐ Featured</span>}
                            {product.stockQuantity > 0 ? (
                                <span className="badge badge-success">In Stock</span>
                            ) : (
                                <span className="badge badge-error">Out of Stock</span>
                            )}
                        </div>

                        <h1 className="product-title">{product.name}</h1>

                        <div className="product-price-section">
                            <span className="product-price">{formatCurrency(product.price)}</span>
                            {product.originalPrice && product.originalPrice > product.price && (
                                <>
                                    <span className="product-price-original">{formatCurrency(product.originalPrice)}</span>
                                    <span className="product-discount">
                                        {Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100)}% OFF
                                    </span>
                                </>
                            )}
                        </div>

                        <div className="product-description">
                            <h3>Description</h3>
                            <p>{product.description}</p>
                        </div>

                        {product.stockQuantity > 0 && (
                            <div className="product-actions">
                                <div className="quantity-selector">
                                    <label htmlFor="quantity">Quantity:</label>
                                    <div className="quantity-controls">
                                        <button
                                            className="quantity-btn"
                                            onClick={() => setQuantity(Math.max(1, quantity - 1))}
                                        >
                                            −
                                        </button>
                                        <input
                                            type="number"
                                            id="quantity"
                                            value={quantity}
                                            onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                                            min="1"
                                            max={product.stockQuantity}
                                            className="quantity-input"
                                        />
                                        <button
                                            className="quantity-btn"
                                            onClick={() => setQuantity(Math.min(product.stockQuantity, quantity + 1))}
                                        >
                                            +
                                        </button>
                                    </div>
                                </div>

                                <button
                                    className="btn btn-lg btn-primary"
                                    onClick={handleAddToCart}
                                    disabled={adding}
                                    style={{ flex: 1 }}
                                >
                                    {adding ? 'Adding...' : '🛒 Add to Cart'}
                                </button>
                            </div>
                        )}

                        <div className="product-meta">
                            <div className="meta-item">
                                <span className="meta-label">Category:</span>
                                <span className="meta-value">{product.category?.name || 'N/A'}</span>
                            </div>
                            <div className="meta-item">
                                <span className="meta-label">Stock:</span>
                                <span className="meta-value">{product.stockQuantity} units</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProductDetailPage;
